package com.helloscala.admin.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.helloscala.common.annotation.AccessLimit;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.dto.user.LoginDTO;
import com.helloscala.common.service.LoginService;
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

    private final LoginService loginService;


    @AccessLimit
    @GetMapping("verify")
    @Operation(summary = "Generate verify code")
    public ResponseResult verify() {
        return loginService.getCaptcha();
    }

    @PostMapping("login")
    public ResponseResult login(@Validated @RequestBody LoginDTO vo) {
        return loginService.login(vo);
    }

    @GetMapping("logout")
    public ResponseResult logout() {
        StpUtil.logout();
        return ResponseResult.success("Logout success!");
    }
}
