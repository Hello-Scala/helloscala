package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.dto.user.SystemUserDTO;
import com.helloscala.common.dto.user.UserPasswordDTO;
import com.helloscala.common.entity.User;

import java.util.List;



public interface UserService extends IService<User> {
    ResponseResult selectUserPage(String username, Integer loginType);

    ResponseResult selectUserById(String id);

    ResponseResult addUser(SystemUserDTO user);

    ResponseResult updateUser(User user);

    ResponseResult deleteUSer(List<String> ids);

    ResponseResult getCurrentUserInfo();

    ResponseResult getCurrentUserMenu();

    ResponseResult updatePassword(UserPasswordDTO userPasswordDTO);

    ResponseResult listOnlineUsers(String keywords);

    ResponseResult kick(String token);
}
