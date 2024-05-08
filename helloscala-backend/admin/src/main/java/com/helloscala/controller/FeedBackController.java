package com.helloscala.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.FeedBack;
import com.helloscala.service.FeedBackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/system/feedback")
@Tag(name = "后台反馈管理")
@RequiredArgsConstructor
public class FeedBackController {

    private final FeedBackService feedBackService;

    @GetMapping(value = "/list")
    @Operation(summary = "反馈列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "反馈列表")
    public ResponseResult selectFeedBackPage(@RequestParam(name = "type", required = false) Integer type) {
        return feedBackService.selectFeedBackPage(type);
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:feedback:delete")
    @Operation(summary = "删除反馈", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "删除反馈")
    @OperationLogger(value = "删除反馈")
    public ResponseResult deleteFeedBack(@RequestBody List<Integer> ids) {
        return feedBackService.deleteFeedBack(ids);
    }

    @PutMapping(value = "/update")
    @OperationLogger(value = "修改反馈")
    @SaCheckPermission("system:feedback:update")
    @Operation(summary = "修改反馈", method = "PUT")
    @ApiResponse(responseCode = "200", description = "修改反馈")
    public ResponseResult update(@RequestBody FeedBack feedBack) {
        return feedBackService.updateFeedBack(feedBack);
    }
}

