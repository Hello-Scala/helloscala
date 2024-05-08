package com.helloscala.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/comment")
@Tag(name = "评论管理")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @Operation(summary = "评论列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "评论列表")
    public ResponseResult selectCommentPage(@RequestParam(name = "keywords", required = false) String keywords){
        return commentService.selectCommentPage(keywords);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:comment:delete")
    @Operation(summary = "批量删除评论", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "批量删除评论")
    @OperationLogger(value = "删除评论")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids){
        return commentService.deleteComment(ids);
    }

}
