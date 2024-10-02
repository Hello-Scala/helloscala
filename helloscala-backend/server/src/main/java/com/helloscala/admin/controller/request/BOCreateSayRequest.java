package com.helloscala.admin.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class BOCreateSayRequest {
    @Schema(name = "图片地址 逗号分隔  最多九张")
    private String imgUrl;

    @Schema(name = "内容")
    private String content;

    @Schema(name = "发表地址。可输入或者地图插件选择")
    private String address;

    @Schema(name = "是否开放查看  0未开放 1开放")
    private Integer isPublic;

    @Schema(name = "创建时间")
    private Date createTime;

    @Schema(name = "修改时间")
    private Date updateTime;
}