package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.FeedBack;
import com.helloscala.common.service.FeedBackService;
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
    public ResponseResult selectFeedBackPage(@RequestParam(name = "type", required = false) Integer type) {
        return feedBackService.selectFeedBackPage(type);
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:feedback:delete")
    @Operation(summary = "Delete feedback", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete feedback")
    @OperationLogger(value = "Delete feedback")
    public ResponseResult deleteFeedBack(@RequestBody List<Integer> ids) {
        return feedBackService.deleteFeedBack(ids);
    }

    @PutMapping(value = "/update")
    @OperationLogger(value = "Update feedback")
    @SaCheckPermission("system:feedback:update")
    @Operation(summary = "Update feedback", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update feedback")
    public ResponseResult update(@RequestBody FeedBack feedBack) {
        return feedBackService.updateFeedBack(feedBack);
    }
}

