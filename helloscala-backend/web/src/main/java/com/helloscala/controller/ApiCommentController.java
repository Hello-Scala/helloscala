package com.helloscala.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.helloscala.annotation.AccessLimit;
import com.helloscala.annotation.BusinessLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Comment;
import com.helloscala.service.ApiCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/comment")
@Tag(name = "评论API-V1")
@RequiredArgsConstructor
public class ApiCommentController {

    private final ApiCommentService commentService;

    @AccessLimit
    @SaCheckLogin
    @BusinessLogger(value = "评论模块-用户评论",type = "添加",desc = "用户评论")
    @RequestMapping(value = "/",method = RequestMethod.POST)
    @Operation(summary = "添加评论", method = "POST")
    @ApiResponse(responseCode = "200", description = "添加评论")
    public ResponseResult addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }

    @RequestMapping(value = "/",method = RequestMethod.GET)
    @Operation(summary = "根据文章id获取相关评论", method = "GET")
    @ApiResponse(responseCode = "200", description = "根据文章id获取相关评论")
    public ResponseResult selectCommentByArticleId(@RequestParam(name = "articleId", required = true) Long articleId){
        return commentService.selectCommentByArticleId(articleId);
    }

    @RequestMapping(value = "/getMyComment",method = RequestMethod.GET)
    @Operation(summary = "获取我的评论", method = "GET")
    @ApiResponse(responseCode = "200", description = "获取我的评论")
    public ResponseResult selectMyComment(){
        return commentService.selectMyComment();
    }
}
