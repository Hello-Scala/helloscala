package com.helloscala.web.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.helloscala.common.annotation.BusinessLogger;
import com.helloscala.common.entity.FeedBack;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.ResponseHelper;
import com.helloscala.web.service.ApiFeedBackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/feedback")
@RequiredArgsConstructor
@Tag(name = "Feedback API-V1")
public class ApiFeedBackController {

    private final ApiFeedBackService feedBackService;

    @SaCheckLogin
    @PostMapping(value = "/")
    @BusinessLogger(value = "Feedback", type = "add", desc = "add feedback")
    @Operation(summary = "Add feedback", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add feedback")
    public EmptyResponse addFeedback(@RequestBody FeedBack feedBack) {
        feedBackService.addFeedback(feedBack);
        return ResponseHelper.ok();
    }

}
