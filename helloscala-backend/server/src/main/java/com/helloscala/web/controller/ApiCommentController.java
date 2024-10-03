package com.helloscala.web.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.annotation.AccessLimit;
import com.helloscala.common.annotation.BusinessLogger;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import com.helloscala.web.controller.comment.APIAddCommentRequest;
import com.helloscala.web.controller.comment.APICommentView;
import com.helloscala.web.service.APICommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/comment")
@Tag(name = "Comment API-V1")
@RequiredArgsConstructor
public class ApiCommentController {

    private final APICommentService commentService;

    @AccessLimit
    @SaCheckLogin
    @BusinessLogger(value = "Comment", type = "add", desc = "add comment")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    @Operation(summary = "add comment", method = "POST")
    @ApiResponse(responseCode = "200", description = "add comment")
    public Response<APICommentView> addComment(@RequestBody APIAddCommentRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        APICommentView commentView = commentService.create(userId, request);
        return ResponseHelper.ok(commentView);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @Operation(summary = "Get article comments", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get article comments")
    public Response<Page<APICommentView>> listByArticleId(@RequestParam(name = "articleId", required = true) String articleId) {
        Page<APICommentView> apiCommentListVOPage = commentService.listByArticleId(articleId);
        return ResponseHelper.ok(apiCommentListVOPage);
    }

    @RequestMapping(value = "/getMyComment", method = RequestMethod.GET)
    @Operation(summary = "Get self comment", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get self comment")
    public Response<Page<APICommentView>> selectMyComment() {
        String userId = StpUtil.getLoginIdAsString();
        Page<APICommentView> commentViewPage = commentService.listByUserId(userId);
        return ResponseHelper.ok(commentViewPage);
    }
}
