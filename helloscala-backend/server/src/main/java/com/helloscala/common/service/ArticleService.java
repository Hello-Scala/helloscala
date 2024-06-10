package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.dto.article.ArticleDTO;
import com.helloscala.common.entity.Article;

import java.util.List;

public interface ArticleService extends IService<Article> {
    ResponseResult selectArticlePage(String title,Integer tagId,Integer categoryId,Integer isPublish);

    ResponseResult selectArticleById(Long id);

    ResponseResult addArticle(ArticleDTO article);

    ResponseResult updateArticle(ArticleDTO article);

    ResponseResult deleteBatchArticle(List<Long> ids);

    ResponseResult topArticle(ArticleDTO article);

    ResponseResult psArticle(Article article);

    ResponseResult seoArticle(List<Long> ids);

    ResponseResult retch(String url);

    ResponseResult randomImg();

}
