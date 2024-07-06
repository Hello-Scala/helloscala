package com.helloscala.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.entity.Comment;
import com.helloscala.common.vo.article.RecommendedArticleVO;
import com.helloscala.common.vo.message.ApiCommentListVO;
import com.helloscala.common.vo.message.SystemCommentVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentMapper extends BaseMapper<Comment> {


    Page<SystemCommentVO> selectPageList(@Param("page")Page<Object> objectPage, @Param("keywords")String keywords);

    Page<ApiCommentListVO> selectCommentPage(@Param("page") Page<ApiCommentListVO> commentListVOPage,
                                             @Param("articleId") Long articleId);

    Page<RecommendedArticleVO> selectMyComment(Page<RecommendedArticleVO> apiArticleListVOPage, @Param("userId") String loginIdAsString);
}
