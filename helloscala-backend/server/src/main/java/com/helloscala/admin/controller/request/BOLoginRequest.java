package com.helloscala.admin.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BOLoginRequest {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private Boolean rememberMe;

    @NotBlank(message = "验证码")
    private String captchaCode;

    @NotBlank(message = "验证码缓存key")
    private String captchaKey;
}
