package com.helloscala.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.FeedBack;
import com.helloscala.service.ApiFeedBackService;
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
@Tag(name = "反馈API-V1")
public class ApiFeedBackController {

    private final ApiFeedBackService feedBackService;

    @SaCheckLogin
    @PostMapping(value = "/")
    @Operation(summary = "添加反馈", method = "POST")
    @ApiResponse(responseCode = "200", description = "添加反馈")
    public ResponseResult addFeedback(@RequestBody FeedBack feedBack) {
        return  feedBackService.addFeedback(feedBack);
    }

}
