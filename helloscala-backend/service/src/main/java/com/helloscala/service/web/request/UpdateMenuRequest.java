package com.helloscala.service.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class UpdateMenuRequest {
    @Schema(name = "ID")
    private String id;

    @Schema(name = "上级资源ID")
    private String parentId;

    @Schema(name = "路由路径")
    private String path;

    @Schema(name = "组件路径")
    private String component;

    @Schema(name = "菜单名称")
    private String title;

    @Schema(name = "排序")
    private Integer sort;

    @Schema(name = "资源图标")
    private String icon;

    @Schema(name = "类型 menu、button")
    private String type;

    @Schema(name = "资源名字")
    private String name;

    @Schema(name = "权限标识")
    private String perm;

    @Schema(name = "是否显示")
    private Integer hidden;

    private String requestBy;
}
