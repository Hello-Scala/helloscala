package com.helloscala.web.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.annotation.AccessLimit;
import com.helloscala.common.annotation.BusinessLogger;
import com.helloscala.common.entity.Comment;
import com.helloscala.common.vo.article.ListArticleVO;
import com.helloscala.common.vo.message.ApiCommentListVO;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import com.helloscala.web.service.ApiCommentService;
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

    private final ApiCommentService commentService;

    @AccessLimit
    @SaCheckLogin
    @BusinessLogger(value = "Comment",type = "add",desc = "add comment")
    @RequestMapping(value = "/",method = RequestMethod.POST)
    @Operation(summary = "add comment", method = "POST")
    @ApiResponse(responseCode = "200", description = "add comment")
    public Response<Comment> addComment(@RequestBody Comment comment){
        Comment commentResult = commentService.addComment(comment);
        return ResponseHelper.ok(commentResult);
    }

    @RequestMapping(value = "/",method = RequestMethod.GET)
    @Operation(summary = "Get article comments", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get article comments")
    public Response<Page<ApiCommentListVO>> selectCommentByArticleId(@RequestParam(name = "articleId", required = true) Long articleId){
        Page<ApiCommentListVO> apiCommentListVOPage = commentService.selectCommentByArticleId(articleId);
        return ResponseHelper.ok(apiCommentListVOPage);
    }

    @RequestMapping(value = "/getMyComment",method = RequestMethod.GET)
    @Operation(summary = "Get self comment", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get self comment")
    public Response<Page<ListArticleVO>> selectMyComment(){
        Page<ListArticleVO> listArticleVOPage = commentService.selectMyComment();
        return ResponseHelper.ok(listArticleVOPage);
    }
}
