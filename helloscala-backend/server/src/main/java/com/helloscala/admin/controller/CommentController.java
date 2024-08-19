package com.helloscala.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.service.CommentService;
import com.helloscala.common.vo.message.SystemCommentVO;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
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
    public Response<Page<SystemCommentVO>> selectCommentPage(@RequestParam(name = "keywords", required = false) String keywords){
        Page<SystemCommentVO> commentPage = commentService.selectCommentPage(keywords);
        return ResponseHelper.ok(commentPage);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:comment:delete")
    @Operation(summary = "Batch delete", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Batch delete")
    @OperationLogger(value = "Batch delete")
    public EmptyResponse deleteBatch(@RequestBody List<String> ids){
        commentService.deleteComment(ids);
        return ResponseHelper.ok();
    }
}
