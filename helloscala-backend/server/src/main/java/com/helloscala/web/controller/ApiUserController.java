package com.helloscala.web.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.helloscala.common.annotation.BusinessLogger;
import com.helloscala.common.dto.user.UserInfoDTO;
import com.helloscala.common.vo.user.UserCountView;
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

@RequestMapping("/user")
@RestController
@Tag(name = "User API")
@RequiredArgsConstructor
public class ApiUserController {
    private final ApiUserService userService;

    @SaCheckLogin
    @BusinessLogger(value = "Website get userinfo",type = "search",desc = "Website get userinfo")
    @GetMapping
    public Response<UserInfoVO> get(@RequestParam(name = "userId", required = true) String userId){
        UserInfoVO userInfoVO = userService.selectUserInfo(userId);
        return ResponseHelper.ok(userInfoVO);
    }

    @SaCheckLogin
    @BusinessLogger(value = "Update userinfo",type = "update",desc = "Update userinfo")
    @PutMapping
    public EmptyResponse update(@RequestBody UserInfoDTO vo){
        userService.updateUser(vo);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "/current",method = RequestMethod.GET)
    @Operation(summary = "Get current user", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get current user")
    public Response<UserInfoVO> getCurrent(){
        String tokenValue = StpUtil.getTokenValue();
        UserInfoVO userInfoVO = userService.selectUserInfoByToken(tokenValue);
        return ResponseHelper.ok(userInfoVO);
    }

    @RequestMapping(value = "count",method = RequestMethod.GET)
    @Operation(summary = "Get user counts", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get user counts")
    public Response<UserCountView> getUserCounts(@RequestParam(name = "id", required = true) String id){
        UserCountView userCount = userService.getUserCounts(id);
        return ResponseHelper.ok(userCount);
    }
}