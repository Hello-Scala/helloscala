package com.helloscala.web.service;

import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Comment;

public interface ApiCommentService {
    ResponseResult addComment(Comment comment);

    ResponseResult selectCommentByArticleId(Long articleId);

    ResponseResult selectMyComment();
}
