package com.helloscala.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.dto.article.ArticlePostDTO;
import com.helloscala.common.entity.Tag;
import com.helloscala.common.vo.article.ApiArticleSearchVO;
import com.helloscala.common.vo.article.ListArticleVO;
import com.helloscala.common.vo.article.ArticleInfoVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ApiArticleService {
    Page<ListArticleVO> selectArticleList(Integer categoryId, Integer tagId, String orderByDescColumn);

    ArticleInfoVO selectArticleInfo(Integer id);

    Page<ApiArticleSearchVO> searchArticle(String keywords);

    Map<Long, List<Tag>> getArticleTagListMap(Set<Long> articleIdSet);

    ResponseResult archive();

    void articleLike(Integer articleId);

    void checkCode(String code);

    void insertArticle(ArticlePostDTO dto);

    Page<ListArticleVO> selectArticleByUserId(String userId, Integer type);

    void deleteMyArticle(Long id);

    ArticlePostDTO selectMyArticleInfo(Long id);

    void updateMyArticle(ArticlePostDTO dto);

    Map<String, Object> readMarkdownFile(MultipartFile file);
}
