package com.helloscala.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.entity.Article;
import com.helloscala.common.vo.article.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ArticleMapper extends BaseMapper<Article> {
    List<SystemArticleContributeVO> contribute(@Param("lastTime") String lastTime, @Param("nowTime") String nowTime);

    Page<RecommendedArticleVO> selectPublicArticleList(Page<Object> page, @Param("categoryId") String categoryId, @Param("tagId") String tagId,
                                                       @Param("orderByDescColumn") String orderByDescColumn);

    ArticleInfoVO selectArticleByIdToVO(String id);

    List<ArticleVO> selectListByBanner();

    List<RecommendedArticleVO> selectRecommendArticle();

    Page<ApiArticleSearchVO> selectSearchArticle(@Param("page") Page<Object> objectPage, @Param("keywords") String keywords);

    Page<RecommendedArticleVO> selectMyArticle(@Param("page") Page<Object> objectPage, @Param("userId") String userId, @Param("type") Integer type);
}
