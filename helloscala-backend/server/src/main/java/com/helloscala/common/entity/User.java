package com.helloscala.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@TableName("b_user")
@Schema(name="User对象", description="系统管理-用户基础信息表")
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Schema(name = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @Schema(name = "账号")
    private String username;

    @Schema(name = "登录密码")
    private String password;

    @Schema(name = "状态")
    private Integer status;

    @Schema(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(name = "最后更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @Schema(name = "最后登录时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date lastLoginTime;

    @Schema(name = "角色ID")
    private Integer roleId;

    @Schema(name = "IP地址")
    private String ipAddress;

    @Schema(name = "IP来源")
    private String ipSource;

    @Schema(name = "登录系统")
    private String os;

    @Schema(name = "浏览器")
    private String browser;

    @Schema(name = "登录类型")
    private Integer loginType;

    @Schema(name = "用户昵称")
    private String nickname;

    @Schema(name = "头像")
    private String avatar;

    @Schema(name = "个人简介")
    private String intro;

    @Schema(name = "个人站点")
    private String webSite;

    @Schema(name = "背景封面")
    private String bjCover;
}
