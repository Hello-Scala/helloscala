package com.helloscala.admin.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.helloscala.admin.controller.request.BOLoginRequest;
import com.helloscala.admin.controller.view.BOVerificationCodeView;
import com.helloscala.admin.service.BOUserSessionService;
import com.helloscala.common.annotation.AccessLimit;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Login management")
@RequiredArgsConstructor
public class LoginController {
    private final BOUserSessionService userSessionService;


    @AccessLimit
    @GetMapping("verify")
    @Operation(summary = "Generate verify code")
    public Response<BOVerificationCodeView> verify() {
        BOVerificationCodeView verificationCodeView = userSessionService.getCaptcha();
        return ResponseHelper.ok(verificationCodeView);
    }

    @PostMapping("login")
    public Response<String> login(@Validated @RequestBody BOLoginRequest request) {
        String token = userSessionService.login(request);
        return ResponseHelper.ok(token);
    }

    @GetMapping("logout")
    public EmptyResponse logout() {
        StpUtil.logout();
        return ResponseHelper.ok();
    }
}
