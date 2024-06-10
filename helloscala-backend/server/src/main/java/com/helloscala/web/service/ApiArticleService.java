package com.helloscala.web.service;

import com.helloscala.common.ResponseResult;
import com.helloscala.common.dto.article.ArticlePostDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ApiArticleService {
    ResponseResult selectArticleList(Integer categoryId, Integer tagId,String orderByDescColumn);

    ResponseResult selectArticleInfo(Integer id);

    ResponseResult searchArticle(String keywords);

    ResponseResult archive();

    ResponseResult articleLike(Integer articleId);

    ResponseResult checkCode(String code);

    ResponseResult insertArticle(ArticlePostDTO dto);

    ResponseResult selectArticleByUserId(String userId, Integer type);

    ResponseResult deleteMyArticle(Long id);

    ResponseResult selectMyArticleInfo(Long id);

    ResponseResult updateMyArticle(ArticlePostDTO dto);

    ResponseResult readMarkdownFile(MultipartFile file);
}
