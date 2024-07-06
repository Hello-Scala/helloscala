package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.dto.article.ArticleDTO;
import com.helloscala.common.entity.Article;
import com.helloscala.common.vo.article.ArticleVO;

import java.util.List;

public interface ArticleService extends IService<Article> {
    Page<ArticleVO> selectArticlePage(String title, Integer tagId, Integer categoryId, Integer isPublish);

    ArticleDTO selectArticleById(Long id);

    void addArticle(ArticleDTO article);

    void updateArticle(ArticleDTO article);

    void deleteBatchArticle(List<Long> ids);

    void topArticle(ArticleDTO article);

    void psArticle(Article article);

    void seoArticle(List<Long> ids);

    void retch(String url);

    String randomImg();

}
