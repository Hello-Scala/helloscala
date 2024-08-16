package com.helloscala.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.dto.article.ArticlePostDTO;
import com.helloscala.common.entity.Article;
import com.helloscala.common.entity.Tag;
import com.helloscala.common.vo.article.ApiArticleSearchVO;
import com.helloscala.common.vo.article.ArticleInfoVO;
import com.helloscala.common.vo.article.RecommendedArticleVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ApiArticleService {
    Page<RecommendedArticleVO> selectArticleList(Integer categoryId, Integer tagId, String orderByDescColumn);

    ArticleInfoVO selectArticleInfo(Long id);

    Page<ApiArticleSearchVO> searchArticle(String keywords);

    Map<Long, List<Tag>> getArticleTagListMap(Set<Long> articleIdSet);

    List<Article> listPublished();

    void articleLike(Integer articleId);

    void checkCode(String code);

    void insertArticle(ArticlePostDTO dto);

    Page<RecommendedArticleVO> listByUserId(String userId, Integer type);

    void deleteMyArticle(Long id);

    ArticlePostDTO getById(Long id);

    void updateMyArticle(ArticlePostDTO dto);
}
