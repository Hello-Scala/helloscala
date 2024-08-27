package com.helloscala.service.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author Steve Zou
 */
@Data
public class UpdateArticleRequest {
    private String title;
    private String avatar;
    private String summary;
    private Integer quantity;
    private String content;
    private String contentMd;

    private String keywords;
    @Schema(name = "阅读方式 0无需验证 1：评论阅读 2：点赞阅读 3：扫码阅读")
    private Integer readType;
    private Integer isStick;
    private Integer isOriginal;
    private String originalUrl;
    private String categoryId;
    private String categoryName;
    private Integer isPublish;

    private Integer isCarousel;

    private Integer isRecommend;

    private List<String> tags;

    private List<String> tagIds;

}
