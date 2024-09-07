package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.service.entity.Comment;
import com.helloscala.common.vo.article.RecommendedArticleVO;
import com.helloscala.common.vo.message.ApiCommentListVO;

public interface ApiCommentService {
    Comment addComment(Comment comment);

    Page<ApiCommentListVO> selectCommentByArticleId(String articleId);

    Page<RecommendedArticleVO> selectMyComment();
}