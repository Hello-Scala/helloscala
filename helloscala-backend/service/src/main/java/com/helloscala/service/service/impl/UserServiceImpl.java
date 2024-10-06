package com.helloscala.service.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.utils.AesEncryptUtil;
import com.helloscala.common.utils.BeanCopyUtil;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.web.exception.BadRequestException;
import com.helloscala.common.web.exception.ConflictException;
import com.helloscala.common.web.exception.NotFoundException;
import com.helloscala.service.entity.User;
import com.helloscala.service.mapper.UserMapper;
import com.helloscala.service.service.MenuService;
import com.helloscala.service.service.RedisConstants;
import com.helloscala.service.service.RedisService;
import com.helloscala.service.service.UserService;
import com.helloscala.service.web.request.UpdateLoginRequest;
import com.helloscala.service.web.view.UserView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

import static com.helloscala.common.ResultCode.ERROR_USER_NOT_EXIST;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final AesEncryptUtil aesEncryptUtil;
    private final MenuService menuService;
    private final RedisService redisService;

    @Override
    public UserView getByNameAndPwd(String userName, String pwd) {
        User user = baseMapper.selectNameAndPassword(userName, pwd);
        return buildUserView(user);
    }

    @Override
    public void updateLogin(UpdateLoginRequest request) {
        LambdaQueryWrapper<User> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(User::getId, request.getId());
        User userToUpdate = new User();
        userToUpdate.setId(request.getId());
        userToUpdate.setIpAddress(request.getIp());
        userToUpdate.setIpSource(request.getCity());
        userToUpdate.setOs(request.getOs());
        userToUpdate.setBrowser(request.getBrowser());
        userToUpdate.setUpdateTime(new Date());
        baseMapper.updateById(userToUpdate);
    }

    private static @NotNull UserView buildUserView(User user) {
        UserView userView = new UserView();
        userView.setId(user.getId());
        userView.setUsername(user.getUsername());
        userView.setStatus(user.getStatus());
        userView.setCreateTime(user.getCreateTime());
        userView.setUpdateTime(user.getUpdateTime());
        userView.setLastLoginTime(user.getLastLoginTime());
        userView.setRoleId(user.getRoleId());
        userView.setIpAddress(user.getIpAddress());
        userView.setIpSource(user.getIpSource());
        userView.setOs(user.getOs());
        userView.setBrowser(user.getBrowser());
        userView.setLoginType(user.getLoginType());
        userView.setNickname(user.getNickname());
        userView.setAvatar(user.getAvatar());
        userView.setIntro(user.getIntro());
        userView.setWebSite(user.getWebSite());
        userView.setBjCover(user.getBjCover());
        return userView;
    }

    @Override
    public Page<SystemUserInfoVO> selectUserPage(String username, Integer loginType) {
        Page<SystemUserInfoVO> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        return baseMapper.selectPageRecord(page, username, loginType);
    }

    @Override
    public UserView get(String id) {
        User user = baseMapper.selectById(id);
        return buildUserView(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User addUser(SystemUserDTO dto) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, dto.getUsername());
        Long count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new ConflictException("Username exist!");
        }
        User user = BeanCopyUtil.copyObject(dto, User.class);
        user.setPassword(aesEncryptUtil.aesEncrypt(user.getPassword()));
        baseMapper.insert(user);
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(User user) {
        baseMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<String> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public SystemUserVO getCurrentUserInfo() {
        SystemUserVO user = baseMapper.getById(StpUtil.getLoginIdAsString());
        List<String> list = menuService.selectButtonPermissions(user.getId());
        user.setPerms(list);
        return user;
    }

    @Override
    public SystemUserVO getWithPermissions(String id) {
        SystemUserVO user = baseMapper.getById(StpUtil.getLoginIdAsString());
        List<String> list = menuService.selectButtonPermissions(user.getId());
        user.setPerms(list);
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(UserPasswordDTO passwordDTO) {
        User user = baseMapper.selectById(StpUtil.getLoginIdAsString());
        if (ObjectUtils.isEmpty(user)) {
            throw new NotFoundException(ERROR_USER_NOT_EXIST.getDesc());
        }

        boolean isValid = aesEncryptUtil.validate(user.getPassword(), passwordDTO.getOldPassword());
        if (!isValid) {
            throw new BadRequestException("Original password invalid!");
        }

        String newPassword = aesEncryptUtil.aesEncrypt(passwordDTO.getNewPassword());
        user.setPassword(newPassword);
        baseMapper.updateById(user);
    }

    @Override
    public Map<String, Object> listOnlineUsers(String keywords) {

        int pageNo = PageUtil.getPageNo().intValue();
        int pageSize = PageUtil.getPageSize().intValue();

        Collection<String> keys = redisService.keys(RedisConstants.LOGIN_TOKEN.concat("*"));
        List<String> totalList = new ArrayList<>(keys);
        int fromIndex = (pageNo - 1) * pageSize;
        int toIndex = totalList.size() - fromIndex > pageSize ? fromIndex + pageSize : totalList.size();
        List<String> onlineUserList = totalList.subList(fromIndex, toIndex);

        List<OnlineUser> resultList = new ArrayList<>();
        for (String key : onlineUserList) {
            Object userObj = redisService.getCacheObject(key);
            OnlineUser onlineUser = JSONUtil.toBean(userObj.toString(), OnlineUser.class);
            resultList.add(onlineUser);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", totalList.size());
        map.put("records", resultList);
        return map;
    }

    @Override
    public void kick(String token) {
        log.info("当前踢下线的用户token为:{}", token);
        StpUtil.logoutByTokenValue(token);
        redisService.deleteObject(RedisConstants.LOGIN_TOKEN.concat(token));
    }

    @Override
    public List<User> listByIds(Set<String> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return List.of();
        }
        return baseMapper.selectBatchIds(ids);
    }

    @Override
    public Long countAll() {
        return baseMapper.selectCount(null);
    }
}
