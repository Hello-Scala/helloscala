package com.helloscala.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.helloscala.annotation.AccessLimit;
import com.helloscala.common.ResponseResult;
import com.helloscala.service.ApiFollowedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/followed")
@RequiredArgsConstructor
@Tag(name = "关注API-V1")
public class ApiFollowedController {

    private final ApiFollowedService followedService;

    @AccessLimit
    @SaCheckLogin
    @PostMapping(value = "/insertFollowed")
    @Operation(summary = "关注用户", method = "POST")
    @ApiResponse(responseCode = "200", description = "关注用户")
    public ResponseResult addFollowedUser(@RequestParam(name = "userId", required = true) String userId) {
        return followedService.addFollowedUser(userId);
    }

    @AccessLimit
    @SaCheckLogin
    @DeleteMapping(value = "/deleteFollowed")
    @Operation(summary = "取消关注用户", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "取消关注用户")
    public ResponseResult deleteFollowed(@RequestParam(name = "userId", required = true) String userId) {
        return followedService.deleteFollowed(userId);
    }
}
