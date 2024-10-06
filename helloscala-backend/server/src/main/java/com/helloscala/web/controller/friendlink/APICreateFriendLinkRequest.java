package com.helloscala.web.controller.friendlink;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class APICreateFriendLinkRequest {
    @Schema(name = "网站名称")
    private String name;

    @Schema(name = "网站地址")
    private String url;

    @Schema(name = "网站头像地址")
    private String avatar;

    @Schema(name = "网站描述")
    private String info;

    @Schema(name = "邮箱")
    private String email;

    @Schema(name = "排序")
    private Integer sort;

    @Schema(name = "状态( 0,下架;1,申请;2:上架)")
    private Integer status;

    @Schema(name = "下架原因")
    private String reason;
}
