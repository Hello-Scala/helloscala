package com.helloscala.service.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateDictRequest {
    @Schema(name = "字典名称")
    private String name;

    @Schema(name = "字典类型")
    private String type;

    @Schema(name = "状态(1:正常，0:停用)")
    private Integer status;

    @Schema(name = "备注")
    private String remark;

    @Schema(name = "排序")
    private Integer sort;

    private String requestBy;
}
