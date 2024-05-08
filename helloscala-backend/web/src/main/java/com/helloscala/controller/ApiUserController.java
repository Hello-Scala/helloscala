package com.helloscala.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.helloscala.annotation.BusinessLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.dto.user.UserInfoDTO;
import com.helloscala.service.ApiUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/user")
@RestController
@Tag(name = "登录接口")
@RequiredArgsConstructor
public class ApiUserController {

    private final ApiUserService userService;

    @SaCheckLogin
    @BusinessLogger(value = "个人中心模块-获取用户信息",type = "修改",desc = "获取用户信息")
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public ResponseResult selectUserInfo(@RequestParam(name = "userId", required = true) String userId){
        return userService.selectUserInfo(userId);
    }

    @SaCheckLogin
    @BusinessLogger(value = "个人中心模块-修改用户信息",type = "修改",desc = "修改用户信息")
    @RequestMapping(value = "/",method = RequestMethod.PUT)
    public ResponseResult updateUser(@RequestBody UserInfoDTO vo){
        return userService.updateUser(vo);
    }

    @RequestMapping(value = "selectUserInfoByToken",method = RequestMethod.GET)
    @Operation(summary = "根据token获取用户信息", method = "GET")
    @ApiResponse(responseCode = "200", description = "根据token获取用户信息")
    public ResponseResult selectUserInfoByToken(@RequestParam(name = "token", required = true) String token){
        return userService.selectUserInfoByToken(token);
    }

    @RequestMapping(value = "getUserCount",method = RequestMethod.GET)
    @Operation(summary = "根据用户id统计用户文章、关注、粉丝等信息", method = "GET")
    @ApiResponse(responseCode = "200", description = "根据用户id统计用户文章、关注、粉丝等信息")
    public ResponseResult getUserCount(@RequestParam(name = "id", required = true) String id){
        return userService.getUserCount(id);
    }
}

