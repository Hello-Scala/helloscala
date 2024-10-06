package com.helloscala.web.service.helper;

import com.helloscala.service.web.view.ArticleDetailView;
import com.helloscala.service.web.view.TagView;
import com.helloscala.web.controller.article.view.APIArticleView;
import org.jetbrains.annotations.NotNull;

/**
 * @author stevezou
 */
public final class APIArticleHelper {
    public static @NotNull APIArticleView buildArticleView(ArticleDetailView article) {
        APIArticleView articleView = new APIArticleView();
        articleView.setId(article.getId());
        articleView.setUserId(article.getUserId());
        articleView.setTitle(article.getTitle());
        articleView.setSummary(article.getSummary());
        articleView.setAvatar(article.getAvatar());
        articleView.setCategoryId(article.getCategoryId());
        articleView.setIsPublish(article.getIsPublish());
        articleView.setIsOriginal(article.getIsOriginal());
        articleView.setOriginalUrl(article.getOriginalUrl());
        articleView.setContent(article.getContent());
        articleView.setContentMd(article.getContentMd());
        articleView.setKeywords(article.getKeywords());
        articleView.setTagList(article.getTags().stream().map(TagView::getName).toList());
        return articleView;
    }
}
