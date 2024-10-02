package com.helloscala.service.web.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class MessageView {
    @Schema(name = "ID")
    private Integer id;

    @Schema(name = "内容")
    private String content;

    @Schema(name = "昵称")
    private String nickname;

    @Schema(name = "头像")
    private String avatar;

    @Schema(name = "ip地址")
    private String ipAddress;

    private Integer time;

    @Schema(name = "ip来源")
    private String ipSource;

    @Schema(name = "状态")
    private Integer status;

    @Schema(name = "创建时间")
    private Date createTime;
}
