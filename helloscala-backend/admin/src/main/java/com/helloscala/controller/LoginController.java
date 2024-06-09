package com.helloscala.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.helloscala.annotation.AccessLimit;
import com.helloscala.common.ResponseResult;
import com.helloscala.dto.user.LoginDTO;
import com.helloscala.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "登录-接口")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;


    @AccessLimit
    @GetMapping("verify")
    @Operation(summary = "生成验证码")
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
