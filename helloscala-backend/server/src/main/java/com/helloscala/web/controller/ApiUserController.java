package com.helloscala.web.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.helloscala.common.annotation.BusinessLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.dto.user.UserInfoDTO;
import com.helloscala.web.service.ApiUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/user")
@RestController
@Tag(name = "User API")
@RequiredArgsConstructor
public class ApiUserController {

    private final ApiUserService userService;

    @SaCheckLogin
    @BusinessLogger(value = "Website get userinfo",type = "search",desc = "Website get userinfo")
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public ResponseResult selectUserInfo(@RequestParam(name = "userId", required = true) String userId){
        return userService.selectUserInfo(userId);
    }

    @SaCheckLogin
    @BusinessLogger(value = "Update userinfo",type = "update",desc = "Update userinfo")
    @RequestMapping(value = "/",method = RequestMethod.PUT)
    public ResponseResult updateUser(@RequestBody UserInfoDTO vo){
        return userService.updateUser(vo);
    }

    @RequestMapping(value = "selectUserInfoByToken",method = RequestMethod.GET)
    @Operation(summary = "Get userinfo by token", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get userinfo by token")
    public ResponseResult selectUserInfoByToken(@RequestParam(name = "token", required = true) String token){
        return userService.selectUserInfoByToken(token);
    }

    @RequestMapping(value = "getUserCount",method = RequestMethod.GET)
    @Operation(summary = "Get user counts", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get user counts")
    public ResponseResult getUserCount(@RequestParam(name = "id", required = true) String id){
        return userService.getUserCount(id);
    }
}