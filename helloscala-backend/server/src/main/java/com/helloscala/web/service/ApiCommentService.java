package com.helloscala.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.entity.Comment;
import com.helloscala.common.vo.article.ListArticleVO;
import com.helloscala.common.vo.message.ApiCommentListVO;

public interface ApiCommentService {
    Comment addComment(Comment comment);

    Page<ApiCommentListVO> selectCommentByArticleId(Long articleId);

    Page<ListArticleVO> selectMyComment();
}
