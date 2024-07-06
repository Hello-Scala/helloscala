package com.helloscala.web.response;

import com.helloscala.common.entity.Tag;
import com.helloscala.common.vo.article.ArticleVO;
import com.helloscala.common.vo.article.RecommendedArticleVO;
import lombok.Data;

import java.util.List;

/**
 * @author Steve Zou
 */
@Data
public class GetHomeInfoResponse {
    private List<ArticleVO> bannerArticles;

    private List<RecommendedArticleVO> recommendedArticles;

    private List<Tag> tags;
}
