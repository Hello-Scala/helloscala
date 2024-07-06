package com.helloscala.web.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.helloscala.common.annotation.BusinessLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.dto.user.UserInfoDTO;
import com.helloscala.common.vo.user.UserInfoVO;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
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
    public Response<UserInfoVO> selectUserInfo(@RequestParam(name = "userId", required = true) String userId){
        UserInfoVO userInfoVO = userService.selectUserInfo(userId);
        return ResponseHelper.ok(userInfoVO);
    }

    @SaCheckLogin
    @BusinessLogger(value = "Update userinfo",type = "update",desc = "Update userinfo")
    @RequestMapping(value = "/",method = RequestMethod.PUT)
    public EmptyResponse updateUser(@RequestBody UserInfoDTO vo){
        userService.updateUser(vo);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "selectUserInfoByToken",method = RequestMethod.GET)
    @Operation(summary = "Get userinfo by token", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get userinfo by token")
    public Response<UserInfoVO> selectUserInfoByToken(@RequestParam(name = "token", required = true) String token){
        UserInfoVO userInfoVO = userService.selectUserInfoByToken(token);
        return ResponseHelper.ok(userInfoVO);
    }

    // todo refactor
    @Deprecated
    @RequestMapping(value = "getUserCount",method = RequestMethod.GET)
    @Operation(summary = "Get user counts", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get user counts")
    public ResponseResult getUserCount(@RequestParam(name = "id", required = true) String id){
        ResponseResult userCount = userService.getUserCount(id);
        return userCount;
    }
}