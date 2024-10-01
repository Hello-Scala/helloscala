package com.helloscala.admin.controller.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class BOUserView {
    @Schema(name = "主键ID")
    private String id;

    @Schema(name = "账号")
    private String username;

    @Schema(name = "状态")
    private Integer status;

    @Schema(name = "创建时间")
    private Date createTime;

    @Schema(name = "最后更新时间")
    private Date updateTime;

    @Schema(name = "最后登录时间")
    private Date lastLoginTime;

    @Schema(name = "角色ID")
    private String roleId;

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
