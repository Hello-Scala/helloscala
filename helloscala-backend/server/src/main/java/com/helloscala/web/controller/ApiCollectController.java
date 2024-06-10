package com.helloscala.web.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.helloscala.common.annotation.AccessLimit;
import com.helloscala.common.ResponseResult;
import com.helloscala.web.service.ApiCollectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/collect")
@RequiredArgsConstructor
@Tag(name = "文章收藏API-V1")
public class ApiCollectController {

    private final ApiCollectService apiCollectService;

    @SaCheckLogin
    @GetMapping(value = "/")
    @Operation(summary = "我的收藏列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "我的收藏列表")
    public ResponseResult selectCollectList() {
        return apiCollectService.selectCollectList();
    }

    @SaCheckLogin
    @AccessLimit
    @GetMapping(value = "collect")
    @Operation(summary = "收藏文章", method = "GET")
    @ApiResponse(responseCode = "200", description = "收藏文章")
    public ResponseResult collect(@RequestParam(name = "articleId", required = true) Integer articleId) {
        return apiCollectService.collect(articleId);
    }

    @SaCheckLogin
    @AccessLimit
    @DeleteMapping(value = "/")
    @Operation(summary = "取消收藏", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "取消收藏")
    public ResponseResult cancel(@RequestParam(name = "articleId", required = true) Integer articleId) {
        return apiCollectService.cancel(articleId);
    }
}
