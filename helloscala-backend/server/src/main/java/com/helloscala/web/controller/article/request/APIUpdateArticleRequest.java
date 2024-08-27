package com.helloscala.web.controller.article.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author Steve Zou
 */
@Data
public class APIUpdateArticleRequest {
    @Schema(name = "id", description = "文章id")
    private String id;

    @Schema(name = "title", description = "文章标题")
    private String title;

    @Schema(name = "summary", description = "文章简介")
    private String summary;

    @Schema(name = "avatar", description = "文章封面")
    private String avatar;

    @Schema(name = "categoryId", description = "文章分类id")
    private String categoryId;

    @Schema(name = "是否发布")
    private Integer isPublish = 2;

    @Schema(name = "是否原创 0：转载 1:原创")
    private Integer isOriginal;

    @Schema(name = "转发地址")
    private String originalUrl;

    @Schema(name = "文章内容")
    private String content;

    @Schema(name = "文章内容MD版")
    private String contentMd;

    @Schema(name = "关键词")
    private String keywords;

    @Schema(name = "userId", description = "用户id")
    private String userId;

    @Schema(name = "文章标签id集合")
    private List<String> tagList;
}
