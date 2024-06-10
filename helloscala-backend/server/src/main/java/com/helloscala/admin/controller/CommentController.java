package com.helloscala.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/comment")
@Tag(name = "Comment management")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @Operation(summary = "List comment", method = "GET")
    @ApiResponse(responseCode = "200", description = "评论列表")
    public ResponseResult selectCommentPage(@RequestParam(name = "keywords", required = false) String keywords){
        return commentService.selectCommentPage(keywords);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:comment:delete")
    @Operation(summary = "Batch delete", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Batch delete")
    @OperationLogger(value = "Batch delete")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids){
        return commentService.deleteComment(ids);
    }
}
