package com.helloscala.admin.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BOCreateDictDataRequest {
    @Schema(name = "字典类型id")
    private Long dictId;

    @Schema(name = "字典标签")
    private String label;

    @Schema(name = "字典键值")
    private String value;

    @Schema(name = "回显样式")
    private String style;

    @Schema(name = "是否默认（1是 0否）")
    private String isDefault;

    @Schema(name = "排序")
    private Integer sort;

    @Schema(name = "是否发布(1:正常，0:停用)")
    private Integer status;

    @Schema(name = "备注")
    private String remark;
}
