package com.helloscala.service.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
@TableName("b_menu")
@Schema(name = "Menu对象", description = "系统管理-权限资源表 ")
public class Menu implements Serializable {
    @Schema(name = "主键")
    @TableId(value = "id", type = IdType.AUTO)
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

    @Schema(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date createdTime;

    @Schema(name = "最后更新时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date updateTime;

    @TableField(exist = false)
    private List<Menu> children;
}
