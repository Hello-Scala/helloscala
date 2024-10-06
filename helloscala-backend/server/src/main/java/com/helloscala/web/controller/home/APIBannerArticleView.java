package com.helloscala.web.controller.home;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class APIBannerArticleView {
    @Schema(name = "主键id")
    private String id;

    @Schema(name = "文章标题")
    private String title;

    @Schema(name = "文章封面地址")
    private String avatar;

    private Date createTime;
}
