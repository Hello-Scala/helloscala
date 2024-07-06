package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.entity.FeedBack;
import com.helloscala.common.service.FeedBackService;
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
@RequestMapping("/system/feedback")
@Tag(name = "Feedback management")
@RequiredArgsConstructor
public class FeedBackController {

    private final FeedBackService feedBackService;

    @GetMapping(value = "/list")
    @Operation(summary = "List feedbacks", method = "GET")
    @ApiResponse(responseCode = "200", description = "List feedbacks")
    public Response<Page<FeedBack>> selectFeedBackPage(@RequestParam(name = "type", required = false) Integer type) {
        Page<FeedBack> feedBackPage = feedBackService.selectFeedBackPage(type);
        return ResponseHelper.ok(feedBackPage);
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:feedback:delete")
    @Operation(summary = "Delete feedback", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete feedback")
    @OperationLogger(value = "Delete feedback")
    public EmptyResponse deleteFeedBack(@RequestBody List<Integer> ids) {
        feedBackService.deleteFeedBack(ids);
        return ResponseHelper.ok();
    }

    @PutMapping(value = "/update")
    @OperationLogger(value = "Update feedback")
    @SaCheckPermission("system:feedback:update")
    @Operation(summary = "Update feedback", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update feedback")
    public EmptyResponse update(@RequestBody FeedBack feedBack) {
        feedBackService.updateFeedBack(feedBack);
        return ResponseHelper.ok();
    }
}

