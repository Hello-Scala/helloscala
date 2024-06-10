package com.helloscala.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.dto.user.SystemUserDTO;
import com.helloscala.common.dto.user.UserPasswordDTO;
import com.helloscala.common.entity.User;
import com.helloscala.common.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/user")
@Tag(name = "System user management")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/list")
    @Operation(summary = "List user", method = "GET")
    @ApiResponse(responseCode = "200", description = "List user")
    public ResponseResult selectUserPage(@RequestParam(name = "username", required = false) String username,
                                         @RequestParam(name = "loginType", required = false) Integer loginType) {
        return userService.selectUserPage(username,loginType);
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("system:user:add")
    @Operation(summary = "Add user", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add user")
    @OperationLogger(value = "Add user")
    public ResponseResult addUser(@RequestBody SystemUserDTO user) {
        return userService.addUser(user);
    }

    @GetMapping(value = "/info/{id}")
    @Operation(summary = "Get user detail", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get user detail")
    public ResponseResult selectUserById(@PathVariable(value = "id", required = true) String id) {
        return userService.selectUserById(id);
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("system:user:update")
    @Operation(summary = "Update user", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update user")
    @OperationLogger(value = "Update user")
    public ResponseResult updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:user:delete")
    @Operation(summary = "Delete user", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete user")
    @OperationLogger(value = "Delete user")
    public ResponseResult deleteUSer(@RequestBody List<String> ids) {
        return userService.deleteUSer(ids);
    }

    @GetMapping(value = "/getCurrentUserInfo")
    @Operation(summary = "Get current user info", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get current user info")
    public ResponseResult getCurrentUserInfo() {
        return userService.getCurrentUserInfo();
    }

    @GetMapping(value = "/getUserMenu")
    @Operation(summary = "Get current user menu", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get current user menu")
    public ResponseResult getCurrentUserMenu() {
        return userService.getCurrentUserMenu();
    }

    @PostMapping(value = "/updatePassword")
    @SaCheckPermission("system:user:updatePassword")
    @Operation(summary = "Update password", method = "POST")
    @ApiResponse(responseCode = "200", description = "Update password")
    @OperationLogger(value = "Update password")
    public ResponseResult updatePassword(@RequestBody UserPasswordDTO userPasswordDTO) {
        return userService.updatePassword(userPasswordDTO);
    }

    @GetMapping(value = "/online")
    @Operation(summary = "Show online users", method = "GET")
    @ApiResponse(responseCode = "200", description = "Show online users")
    public ResponseResult listOnlineUsers(@RequestParam(name = "keywords", required = false) String keywords) {
        return userService.listOnlineUsers(keywords);
    }

    @GetMapping(value = "/kick")
    @SaCheckPermission("system:user:kick")
    @OperationLogger(value = "Make user offline")
    @Operation(summary = "Make user offline", method = "GET")
    @ApiResponse(responseCode = "200", description = "Make user offline")
    public ResponseResult kick(@RequestParam(name = "token", required = true) String token) {
        return userService.kick(token);
    }
}
