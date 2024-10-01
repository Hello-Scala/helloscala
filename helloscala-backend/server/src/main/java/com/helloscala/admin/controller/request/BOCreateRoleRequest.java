package com.helloscala.admin.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BOCreateRoleRequest {

    @Schema(name = "角色编码")
    private String code;

    @Schema(name = "角色名称")
    private String name;

    @Schema(name = "角色描述")
    private String remarks;
}
