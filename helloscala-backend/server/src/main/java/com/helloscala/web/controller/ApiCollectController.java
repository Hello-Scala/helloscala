package com.helloscala.web.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.annotation.AccessLimit;
import com.helloscala.common.vo.article.RecommendedArticleVO;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
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
    public Response<Page<RecommendedArticleVO>> selectCollectList() {
        Page<RecommendedArticleVO> listArticleVOPage = apiCollectService.selectCollectList();
        return ResponseHelper.ok(listArticleVOPage);
    }

    @SaCheckLogin
    @AccessLimit
    @GetMapping(value = "collect")
    @Operation(summary = "收藏文章", method = "GET")
    @ApiResponse(responseCode = "200", description = "收藏文章")
    public EmptyResponse collect(@RequestParam(name = "articleId", required = true) Integer articleId) {
        apiCollectService.collect(articleId);
        return ResponseHelper.ok();
    }

    @SaCheckLogin
    @AccessLimit
    @DeleteMapping(value = "/")
    @Operation(summary = "取消收藏", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "取消收藏")
    public EmptyResponse cancel(@RequestParam(name = "articleId", required = true) Integer articleId) {
        apiCollectService.cancel(articleId);
        return ResponseHelper.ok();
    }
}
