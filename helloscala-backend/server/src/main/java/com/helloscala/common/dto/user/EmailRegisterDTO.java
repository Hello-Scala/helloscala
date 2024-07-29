package com.helloscala.common.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@Schema(description = "邮箱注册信息")
public class EmailRegisterDTO {
    @NotBlank(message = "邮箱不能为空")
    @Schema(name = "email", required = true, type = "String")
    private String email;


    @NotBlank(message = "密码不能为空")
    @Schema(name = "password", required = true, type = "String")
    private String password;

    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @NotBlank(message = "验证码不能为空")
    private String code;

}
