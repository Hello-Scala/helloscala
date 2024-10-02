package com.helloscala.admin.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BOUpdateTagRequest {
    @Schema(name = "ID")
    private String id;

    @Schema(name = "标签名称")
    private String name;

    @Schema(name = "排序")
    private Integer sort;
}
