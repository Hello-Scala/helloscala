package com.helloscala.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.dto.user.SystemUserDTO;
import com.helloscala.common.dto.user.UserPasswordDTO;
import com.helloscala.common.entity.User;
import com.helloscala.common.service.UserService;
import com.helloscala.common.vo.menu.RouterVO;
import com.helloscala.common.vo.user.SystemUserInfoVO;
import com.helloscala.common.vo.user.SystemUserVO;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/user")
@Tag(name = "System user management")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/list")
    @Operation(summary = "List user", method = "GET")
    @ApiResponse(responseCode = "200", description = "List user")
    public Response<Page<SystemUserInfoVO>> selectUserPage(@RequestParam(name = "username", required = false) String username,
                                                           @RequestParam(name = "loginType", required = false) Integer loginType) {
        Page<SystemUserInfoVO> userPage = userService.selectUserPage(username, loginType);
        return ResponseHelper.ok(userPage);
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("system:user:add")
    @Operation(summary = "Add user", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add user")
    @OperationLogger(value = "Add user")
    public Response<User> addUser(@RequestBody SystemUserDTO user) {
        User addedUser = userService.addUser(user);
        return ResponseHelper.ok(addedUser);
    }

    @GetMapping(value = "/info/{id}")
    @Operation(summary = "Get user detail", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get user detail")
    public Response<SystemUserVO> getById(@PathVariable(value = "id", required = true) String id) {
        SystemUserVO user = userService.get(id);
        return ResponseHelper.ok(user);
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("system:user:update")
    @Operation(summary = "Update user", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update user")
    @OperationLogger(value = "Update user")
    public EmptyResponse updateUser(@RequestBody User user) {
        userService.update(user);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @SaCheckPermission("system:user:delete")
    @Operation(summary = "Delete user", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete user")
    @OperationLogger(value = "Delete user")
    public EmptyResponse deleteUSer(@RequestBody List<String> ids) {
        userService.deleteByIds(ids);
        return ResponseHelper.ok();
    }

    @GetMapping(value = "/getCurrentUserInfo")
    @Operation(summary = "Get current user info", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get current user info")
    public Response<SystemUserVO> getCurrentUserInfo() {
        String loginId = StpUtil.getLoginIdAsString();
        SystemUserVO userInfo = userService.getWithPermissions(loginId);
        return ResponseHelper.ok(userInfo);
    }

    @GetMapping(value = "/getUserMenu")
    @Operation(summary = "Get current user menu", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get current user menu")
    public Response<List<RouterVO>> getCurrentUserMenu() {
        List<RouterVO> currentUserMenus = userService.getCurrentUserMenu();
        return ResponseHelper.ok(currentUserMenus);
    }

    @PostMapping(value = "/updatePassword")
    @SaCheckPermission("system:user:updatePassword")
    @Operation(summary = "Update password", method = "POST")
    @ApiResponse(responseCode = "200", description = "Update password")
    @OperationLogger(value = "Update password")
    public EmptyResponse updatePassword(@RequestBody UserPasswordDTO userPasswordDTO) {
        userService.updatePassword(userPasswordDTO);
        return ResponseHelper.ok();
    }

    // todo refactor
    @GetMapping(value = "/online")
    @Operation(summary = "Show online users", method = "GET")
    @ApiResponse(responseCode = "200", description = "Show online users")
    public Response<Map<String, Object>> listOnlineUsers(@RequestParam(name = "keywords", required = false) String keywords) {
        Map<String, Object> map = userService.listOnlineUsers(keywords);
        return ResponseHelper.ok(map);
    }

    @GetMapping(value = "/kick")
    @SaCheckPermission("system:user:kick")
    @OperationLogger(value = "Make user offline")
    @Operation(summary = "Make user offline", method = "GET")
    @ApiResponse(responseCode = "200", description = "Make user offline")
    public EmptyResponse kick(@RequestParam(name = "token", required = true) String token) {
        userService.kick(token);
        return ResponseHelper.ok();
    }
}
