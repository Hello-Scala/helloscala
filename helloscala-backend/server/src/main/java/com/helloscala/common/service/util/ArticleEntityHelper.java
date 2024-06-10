package com.helloscala.common.service.util;

import com.helloscala.common.entity.Article;
import com.helloscala.common.entity.ArticleElastic;
import com.helloscala.common.utils.DateUtil;

/**
 * @author Steve Zou
 */
public final class ArticleEntityHelper {
    public static ArticleElastic toElasticEntity(Article article) {
        ArticleElastic articleElastic = new ArticleElastic();
        articleElastic.setId(article.getId());
        articleElastic.setTitle(article.getTitle());
        articleElastic.setSummary(article.getSummary());
        articleElastic.setIsPublish(article.getIsPublish());
        articleElastic.setCreateTime(DateUtil.dateTimeToStr(article.getCreateTime()));
        return articleElastic;
    }
}
