package com.helloscala.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@TableName("b_role_menu")
@Schema(name = "RoleMenu对象", description = "系统管理 - 角色-权限资源关联表 ")
public class RoleMenu implements Serializable {
    @Schema(name = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Schema(name = "角色ID")
    private String roleId;

    @Schema(name = "菜单ID")
    private String menuId;

    @Schema(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createdTime;

    @Schema(name = "最后更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date lastTime;
}
