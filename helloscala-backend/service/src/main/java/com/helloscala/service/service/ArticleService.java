package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.Article;
import com.helloscala.service.web.request.CreateArticleRequest;
import com.helloscala.service.web.request.ListArticleRequest;
import com.helloscala.service.web.request.SearchArticleRequest;
import com.helloscala.service.web.request.UpdateArticleRequest;
import com.helloscala.service.web.view.ArticleContributeCountView;
import com.helloscala.service.web.view.ArticleDetailView;
import com.helloscala.service.web.view.ArticleView;
import com.helloscala.service.web.view.CategoryArticleCountView;

import java.util.List;
import java.util.Set;

public interface ArticleService extends IService<Article> {
    Page<ArticleView> selectArticlePage(String title, String tagId, String categoryId, Integer isPublish);

    Page<ArticleView> search(Page<?> page, SearchArticleRequest request);

    Page<ArticleView> listArticleSummary(Page<?> page, ListArticleRequest request);

    List<ArticleView> listArticleSummary(ListArticleRequest request);

    Page<ArticleDetailView> listArticleDetail(Page<?> page, ListArticleRequest request);

    ArticleDetailView getDetailById(String id);

    void addArticle(String userId, String ipAddress, CreateArticleRequest request);

    void updateArticle(String userId, String articleId, UpdateArticleRequest request);

    void deleteBatchArticle(List<String> ids);

    int stick(String id, boolean stick);

    void psArticle(Article article);

    void seoArticle(List<String> ids);

    boolean existArticle(String id);

    boolean ownArticle(String userId, String id);

    boolean ownArticles(String userId, Set<String> ids);

    boolean existUnderCategory(String categoryId);

    boolean existAnyUnderCategory(Set<String> categoryIds);

    List<CategoryArticleCountView> countByCategories(Set<String> categoryIds);

    List<CategoryArticleCountView> countAllCategories();

    Long countAll();

    List<ArticleContributeCountView> contributeCount(String startTime, String endTime);
}
