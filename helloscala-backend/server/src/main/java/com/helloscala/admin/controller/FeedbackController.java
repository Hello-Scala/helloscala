package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.request.BOUpdateFeedbackRequest;
import com.helloscala.admin.controller.view.BOFeedbackView;
import com.helloscala.admin.service.BOFeedbackService;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;


@RestController
@RequestMapping("/system/feedback")
@Tag(name = "Feedback management")
@RequiredArgsConstructor
public class FeedbackController {

    private final BOFeedbackService feedBackService;

    @GetMapping(value = "/list")
    @Operation(summary = "List feedbacks", method = "GET")
    @ApiResponse(responseCode = "200", description = "List feedbacks")
    public Response<Page<BOFeedbackView>> selectFeedBackPage(@RequestParam(name = "type", required = false) Integer type) {
        Page<BOFeedbackView> feedBackPage = feedBackService.listByPage(type);
        return ResponseHelper.ok(feedBackPage);
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:feedback:delete")
    @Operation(summary = "Delete feedback", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete feedback")
    @OperationLogger(value = "Delete feedback")
    public EmptyResponse deleteFeedBack(@RequestBody List<String> ids) {
        String userId = StpUtil.getLoginIdAsString();
        feedBackService.deleteBatch(userId, new HashSet<>(ids));
        return ResponseHelper.ok();
    }

    @PutMapping(value = "/update")
    @OperationLogger(value = "Update feedback")
    @SaCheckPermission("system:feedback:update")
    @Operation(summary = "Update feedback", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update feedback")
    public EmptyResponse update(@RequestBody BOUpdateFeedbackRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        feedBackService.update(userId, request);
        return ResponseHelper.ok();
    }
}
