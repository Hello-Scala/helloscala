package com.helloscala.service.entity;

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
@TableName("b_user_role")
@Schema(name="UserRole对象", description="系统管理 - 用户角色关联表 ")
public class UserRole implements Serializable {
    @Schema(name = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "角色ID")
    private Integer roleId;

    @Schema(name = "用户ID")
    private Integer userId;

    @Schema(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createdTime;

    @Schema(name = "最后更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date lastTime;
}
