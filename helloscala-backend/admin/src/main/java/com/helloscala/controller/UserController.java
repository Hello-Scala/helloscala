package com.helloscala.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.dto.user.SystemUserDTO;
import com.helloscala.dto.user.UserPasswordDTO;
import com.helloscala.entity.User;
import com.helloscala.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/user")
@Tag(name = "系统用户管理-接口")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/list")
    @Operation(summary = "用户列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "用户列表")
    public ResponseResult selectUserPage(@RequestParam(name = "username", required = false) String username,
                                         @RequestParam(name = "loginType", required = false) Integer loginType) {
        return userService.selectUserPage(username,loginType);
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("system:user:add")
    @Operation(summary = "添加用户", method = "POST")
    @ApiResponse(responseCode = "200", description = "添加用户")
    @OperationLogger(value = "添加用户")
    public ResponseResult addUser(@RequestBody SystemUserDTO user) {
        return userService.addUser(user);
    }

    @GetMapping(value = "/info/{id}")
    @Operation(summary = "用户详情", method = "GET")
    @ApiResponse(responseCode = "200", description = "用户详情")
    public ResponseResult selectUserById(@PathVariable(value = "id", required = true) String id) {
        return userService.selectUserById(id);
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("system:user:update")
    @Operation(summary = "修改用户", method = "PUT")
    @ApiResponse(responseCode = "200", description = "修改用户")
    @OperationLogger(value = "修改用户")
    public ResponseResult updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:user:delete")
    @Operation(summary = "删除用户", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "删除用户")
    @OperationLogger(value = "删除用户")
    public ResponseResult deleteUSer(@RequestBody List<String> ids) {
        return userService.deleteUSer(ids);
    }

    @GetMapping(value = "/getCurrentUserInfo")
    @Operation(summary = "获取当前登录用户信息", method = "GET")
    @ApiResponse(responseCode = "200", description = "获取当前登录用户信息")
    public ResponseResult getCurrentUserInfo() {
        return userService.getCurrentUserInfo();
    }

    @GetMapping(value = "/getUserMenu")
    @Operation(summary = "获取用户菜单", method = "GET")
    @ApiResponse(responseCode = "200", description = "获取用户菜单")
    public ResponseResult getCurrentUserMenu() {
        return userService.getCurrentUserMenu();
    }

    @PostMapping(value = "/updatePassword")
    @SaCheckPermission("system:user:updatePassword")
    @Operation(summary = "修改密码", method = "POST")
    @ApiResponse(responseCode = "200", description = "修改密码")
    @OperationLogger(value = "修改密码")
    public ResponseResult updatePassword(@RequestBody UserPasswordDTO userPasswordDTO) {
        return userService.updatePassword(userPasswordDTO);
    }

    @GetMapping(value = "/online")
    @Operation(summary = "查看在线用户", method = "GET")
    @ApiResponse(responseCode = "200", description = "查看在线用户")
    public ResponseResult listOnlineUsers(@RequestParam(name = "keywords", required = false) String keywords) {
        return userService.listOnlineUsers(keywords);
    }

    @GetMapping(value = "/kick")
    @SaCheckPermission("system:user:kick")
    @OperationLogger(value = "踢人下线")
    @Operation(summary = "踢人下线", method = "GET")
    @ApiResponse(responseCode = "200", description = "踢人下线")
    public ResponseResult kick(@RequestParam(name = "token", required = true) String token) {
        return userService.kick(token);
    }
}
