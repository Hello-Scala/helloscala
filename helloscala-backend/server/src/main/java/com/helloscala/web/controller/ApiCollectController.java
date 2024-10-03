package com.helloscala.web.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.annotation.AccessLimit;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import com.helloscala.web.controller.article.view.APIRecommendedArticleView;
import com.helloscala.web.service.APIArticleService;
import com.helloscala.web.service.APICollectService;
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

    private final APICollectService apiCollectService;
    private final APIArticleService articleService;

    @SaCheckLogin
    @GetMapping(value = "/")
    @Operation(summary = "我的收藏列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "我的收藏列表")
    public Response<Page<APIRecommendedArticleView>> list() {
        String userId = StpUtil.getLoginIdAsString();
        Page<APIRecommendedArticleView> recommendedArticleViewPage = apiCollectService.listByPage(userId);
        return ResponseHelper.ok(recommendedArticleViewPage);
    }

    @SaCheckLogin
    @AccessLimit
    @GetMapping(value = "collect")
    @Operation(summary = "收藏文章", method = "GET")
    @ApiResponse(responseCode = "200", description = "收藏文章")
    public EmptyResponse collect(@RequestParam(name = "articleId", required = true) String articleId) {
        String userId = StpUtil.getLoginIdAsString();
        apiCollectService.collect(userId, articleId);
        return ResponseHelper.ok();
    }

    @SaCheckLogin
    @AccessLimit
    @DeleteMapping(value = "/")
    @Operation(summary = "取消收藏", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "取消收藏")
    public EmptyResponse cancel(@RequestParam(name = "articleId", required = true) String articleId) {
        String userId = StpUtil.getLoginIdAsString();
        apiCollectService.cancel(userId, articleId);
        return ResponseHelper.ok();
    }
}
