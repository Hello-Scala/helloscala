package com.helloscala.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.dto.article.ArticleDTO;
import com.helloscala.common.dto.article.ArticlePostDTO;
import com.helloscala.common.entity.Article;
import com.helloscala.common.vo.article.ApiArchiveVO;
import com.helloscala.common.vo.article.ArticleInfoVO;
import com.helloscala.common.vo.article.ListArticleVO;
import com.helloscala.common.vo.article.ApiArticleSearchVO;
import com.helloscala.common.vo.article.SystemArticleContributeVO;
import com.helloscala.common.vo.article.SystemArticleListVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ArticleMapper extends BaseMapper<Article> {

    Page<SystemArticleListVO> selectArticle(@Param("page") Page<Object> page, @Param("title")String title, @Param("tagId")Integer tagId,
                                            @Param("categoryId")Integer categoryId, @Param("isPublish")Integer isPublish);

    ArticleDTO selectArticlePrimaryKey(Long id);

    void putTopArticle(@Param("article") ArticleDTO article);


    List<SystemArticleContributeVO> contribute(@Param("lastTime") String lastTime, @Param("nowTime")String nowTime);



    List<ApiArchiveVO> selectListArchive();

    Page<ListArticleVO> selectPublicArticleList(Page<Object> page, @Param("categoryId") Integer categoryId, @Param("tagId")Integer tagId,
                                                @Param("orderByDescColumn")String orderByDescColumn);


    ArticleInfoVO selectArticleByIdToVO(Integer id);

    List<SystemArticleListVO> selectListByBanner();

    List<ListArticleVO> selectRecommendArticle();

    List<ListArticleVO> selectHotArticleList();

    Page<ApiArticleSearchVO> selectSearchArticle(@Param("page") Page<Object> objectPage, @Param("keywords") String keywords);

    Page<ListArticleVO> selectMyArticle(@Param("page")Page<Object> objectPage, @Param("userId") String userId, @Param("type") Integer type);

    ArticlePostDTO selectMyArticleInfo(Long id);
}
