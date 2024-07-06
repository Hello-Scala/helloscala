package com.helloscala.common.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.common.RedisConstants;
import com.helloscala.common.config.satoken.OnlineUser;
import com.helloscala.common.dto.user.SystemUserDTO;
import com.helloscala.common.dto.user.UserPasswordDTO;
import com.helloscala.common.entity.Menu;
import com.helloscala.common.entity.User;
import com.helloscala.common.mapper.UserMapper;
import com.helloscala.common.service.MenuService;
import com.helloscala.common.service.RedisService;
import com.helloscala.common.service.UserService;
import com.helloscala.common.utils.AesEncryptUtil;
import com.helloscala.common.utils.BeanCopyUtil;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.vo.menu.RouterVO;
import com.helloscala.common.vo.user.SystemUserInfoVO;
import com.helloscala.common.vo.user.SystemUserVO;
import com.helloscala.common.web.exception.BadRequestException;
import com.helloscala.common.web.exception.ConflictException;
import com.helloscala.common.web.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public Page<SystemUserInfoVO> selectUserPage(String username, Integer loginType) {
        Page<SystemUserInfoVO> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        return baseMapper.selectPageRecord(page, username, loginType);
    }

    @Override
    public SystemUserVO get(String id) {
        return baseMapper.getById(id);
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
    public List<RouterVO> getCurrentUserMenu() {
        List<Menu> menus;
        if (StpUtil.hasRole(Constants.ADMIN_CODE)) {
            menus = menuService.list();
        } else {
            List<Integer> menuIds = baseMapper.getMenuId(StpUtil.getLoginIdAsString());
            menus = menuService.listByIds(menuIds);
        }
        return menuService.buildRouterTree(menus);
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
}
