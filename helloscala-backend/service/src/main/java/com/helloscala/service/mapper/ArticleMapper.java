package com.helloscala.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.service.entity.Article;
import com.helloscala.common.vo.article.*;
import com.helloscala.service.web.view.ArticleSummaryView;
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

    Page<ArticleSummaryView> selectSearchArticle(@Param("page") Page<?> objectPage, @Param("keywords") String keywords);

    Page<RecommendedArticleVO> selectMyArticle(@Param("page") Page<Object> objectPage, @Param("userId") String userId, @Param("type") Integer type);
}
