package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.dto.user.SystemUserDTO;
import com.helloscala.common.dto.user.UserPasswordDTO;
import com.helloscala.common.entity.User;
import com.helloscala.common.vo.menu.RouterVO;
import com.helloscala.common.vo.user.SystemUserInfoVO;
import com.helloscala.common.vo.user.SystemUserVO;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface UserService extends IService<User> {
    Page<SystemUserInfoVO> selectUserPage(String username, Integer loginType);

    SystemUserVO get(String id);

    User addUser(SystemUserDTO user);

    void update(User user);

    void deleteByIds(List<String> ids);

    SystemUserVO getCurrentUserInfo();

    SystemUserVO getWithPermissions(String id);

    List<RouterVO> getCurrentUserMenu();

    void updatePassword(UserPasswordDTO userPasswordDTO);

    Map<String, Object> listOnlineUsers(String keywords);

    void kick(String token);

    List<User> listByIds(Set<String> ids);
}
