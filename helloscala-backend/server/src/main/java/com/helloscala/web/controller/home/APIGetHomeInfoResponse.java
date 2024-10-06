package com.helloscala.web.controller.home;

import com.helloscala.service.web.view.BannerArticleView;
import com.helloscala.web.controller.article.view.APIArticleView;
import com.helloscala.web.controller.article.view.APIRecommendedArticleView;
import lombok.Data;

import java.util.List;

/**
 * @author Steve Zou
 */
@Data
public class APIGetHomeInfoResponse {
    private List<APIBannerArticleView> bannerArticles;

    private List<APIBannerArticleView> recommendedArticles;

    private List<APITagView> tags;
}
