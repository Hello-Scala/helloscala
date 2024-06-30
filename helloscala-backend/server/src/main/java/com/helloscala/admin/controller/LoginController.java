package com.helloscala.admin.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.helloscala.common.annotation.AccessLimit;
import com.helloscala.common.dto.user.LoginDTO;
import com.helloscala.common.service.LoginService;
import com.helloscala.common.vo.user.VerifyCodeVO;
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

    private final LoginService loginService;


    @AccessLimit
    @GetMapping("verify")
    @Operation(summary = "Generate verify code")
    public Response<VerifyCodeVO> verify() {
        VerifyCodeVO verifyCodeVO = loginService.getCaptcha();
        return ResponseHelper.ok(verifyCodeVO);
    }

    @PostMapping("login")
    public Response<String> login(@Validated @RequestBody LoginDTO vo) {
        String token = loginService.login(vo);
        return ResponseHelper.ok(token);
    }

    @GetMapping("logout")
    public EmptyResponse logout() {
        StpUtil.logout();
        return ResponseHelper.ok();
    }
}
