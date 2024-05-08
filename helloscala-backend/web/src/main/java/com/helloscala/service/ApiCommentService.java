package com.helloscala.service;

import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Comment;

public interface ApiCommentService {

    /**
     * 发表评论
     * @param comment
     * @return
     */
    public ResponseResult addComment(Comment comment);

    /**
     * 分页获取文章评论
     * @param articleId
     * @return
     */
    public ResponseResult selectCommentByArticleId(Long articleId);

    /**
     * 获取我的评论
     * @return
     */
    public ResponseResult selectMyComment();


}