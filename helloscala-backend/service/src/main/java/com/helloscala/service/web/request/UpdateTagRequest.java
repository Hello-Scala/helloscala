package com.helloscala.service.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateTagRequest {
    @Schema(name = "id")
    private String id;

    @Schema(name = "标签名称")
    private String name;

    @Schema(name = "排序")
    private Integer sort;

    private String requestBy;
}
