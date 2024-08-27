package com.helloscala.service.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class CreateCategoryRequest {
    @Schema(name = "分类名称")
    private String name;

    @Schema(name = "排序")
    private Integer sort;

    @Schema(name = "图标")
    private String icon;
}
