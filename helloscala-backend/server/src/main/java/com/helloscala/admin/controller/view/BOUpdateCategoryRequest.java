package com.helloscala.admin.controller.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class BOUpdateCategoryRequest {
    @Schema(name = "主键id")
    private String id;

    @Schema(name = "分类名称")
    private String name;

    @Schema(name = "排序")
    private Integer sort;

    @Schema(name = "图标")
    private String icon;
}
