package com.helloscala.service.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class SearchBannerArticleRequest {
    @Schema(name = "发布状态 0：下架；1：上架")
    private Integer isPublish;

    @Schema(name = "是否首页轮播")
    private Integer isCarousel;

    @Schema(name = "是否推荐 0否 1是")
    private Integer isRecommend;

    private List<SortingRule> sortingRules;

    private Integer limit;
}
