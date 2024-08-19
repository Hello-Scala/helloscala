package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.dto.article.ArticleDTO;
import com.helloscala.common.entity.Article;
import com.helloscala.common.vo.article.ArticleVO;

import java.util.List;

public interface ArticleService extends IService<Article> {
    Page<ArticleVO> selectArticlePage(String title, String tagId, String categoryId, Integer isPublish);

    ArticleDTO selectArticleById(String id);

    void addArticle(String ipAddress, ArticleDTO article);

    void updateArticle(String userId, ArticleDTO article);

    void deleteBatchArticle(List<String> ids);

    int stick(String id, boolean stick);

    void psArticle(Article article);

    void seoArticle(List<String> ids);

    String randomImg();

}
