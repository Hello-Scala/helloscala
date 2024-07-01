package com.helloscala.web.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.helloscala.common.annotation.AccessLimit;
import com.helloscala.common.annotation.BusinessLogger;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.ResponseHelper;
import com.helloscala.web.service.ApiFollowedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/followed")
@RequiredArgsConstructor
@Tag(name = "Follow API-V1")
public class ApiFollowedController {

    private final ApiFollowedService followedService;

    @AccessLimit
    @SaCheckLogin
    @PostMapping(value = "/insertFollowed")
    @BusinessLogger(value = "Followed",type = "add",desc = "add followed user")
    @Operation(summary = "Add Follower", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add Follower")
    public EmptyResponse addFollowedUser(@RequestParam(name = "userId", required = true) String userId) {
        followedService.addFollowedUser(userId);
        return ResponseHelper.ok();
    }

    @AccessLimit
    @SaCheckLogin
    @DeleteMapping(value = "/deleteFollowed")
    @Operation(summary = "Unfollow", method = "DELETE")
    @BusinessLogger(value = "Followed",type = "delete",desc = "Unfollow")
    @ApiResponse(responseCode = "200", description = "Unfollow user")
    public EmptyResponse deleteFollowed(@RequestParam(name = "userId", required = true) String userId) {
        followedService.deleteFollowed(userId);
        return ResponseHelper.ok();
    }
}
