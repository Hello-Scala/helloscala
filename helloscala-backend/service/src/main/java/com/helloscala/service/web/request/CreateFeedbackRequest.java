package com.helloscala.service.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateFeedbackRequest {
    @Schema(name = "用户id")
    private String userId;

    @Schema(name = "标题")
    private String title;

    @Schema(name = "详细内容")
    private String content;

    @Schema(name = "图片地址")
    private String imgUrl;

    @Schema(name = "反馈类型 1:需求 2：缺陷")
    private Integer type;

    @Schema(name = "状态 0:未解决 1：解决")
    private Integer status;

    private String requestBy;
}
