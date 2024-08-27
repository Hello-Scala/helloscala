package com.helloscala.admin.controller.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BOArticleDetailView {
    @Schema(name = "主键id")
    private String id;

    @Schema(name = "用户id")
    private String userId;

    @Schema(name = "文章标题")
    private String title;

    @Schema(name = "文章封面地址")
    private String avatar;

    @Schema(name = "阅读方式 0无需验证 1：评论阅读 2：点赞阅读 3：扫码阅读")
    private Integer readType;

    @Schema(name = "是否置顶 0否 1是")
    private Integer isStick;

    @Schema(name = "是否原创 0：转载 1:原创")
    private Integer isOriginal;

    @Schema(name = "文章阅读量")
    private Integer quantity;

    @Schema(name = "创建时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @Schema(name = "更新时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date updateTime;

    @Schema(name = "状态")
    private Integer isPublish;

    @Schema(name = "分类名")
    private String categoryName;

    private String summary;
    private String content;
    private String contentMd;

    private String keywords;
    private String originalUrl;

    private Integer isCarousel;

    private Integer isRecommend;

    private List<String> tags;
}