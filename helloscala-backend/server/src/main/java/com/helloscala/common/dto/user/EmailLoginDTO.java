package com.helloscala.common.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "邮箱登录信息")
public class EmailLoginDTO {
    /**
     *  邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Schema(name = "email", description = "email", required = true, type = "String")
    private String email;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Schema(name = "password", description = "password", required = true, type = "String")
    private String password;

}
