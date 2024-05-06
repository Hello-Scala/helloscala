package com.helloscala.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.entity.Comment;
import com.helloscala.vo.article.ApiArticleListVO;
import com.helloscala.vo.message.ApiCommentListVO;
import com.helloscala.vo.message.SystemCommentVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentMapper extends BaseMapper<Comment> {


    Page<SystemCommentVO> selectPageList(@Param("page")Page<Object> objectPage, @Param("keywords")String keywords);

    Page<ApiCommentListVO> selectCommentPage(@Param("page") Page<ApiCommentListVO> commentListVOPage,
                                             @Param("articleId") Long articleId);

    /**
     * 获取我的文章
     * @param apiArticleListVOPage
     * @param loginIdAsString 登录用户id
     * @return
     */
    Page<ApiArticleListVO> selectMyComment(Page<ApiArticleListVO> apiArticleListVOPage, @Param("userId") String loginIdAsString);
}
