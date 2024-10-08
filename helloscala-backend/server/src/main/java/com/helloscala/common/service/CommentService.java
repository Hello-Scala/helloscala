package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.entity.Comment;
import com.helloscala.common.vo.message.SystemCommentVO;

import java.util.List;
import java.util.Set;


public interface CommentService extends IService<Comment> {
    Page<SystemCommentVO> selectCommentPage(String keywords);

    void deleteComment(List<String> ids);

    List<Comment> listArticleComment(Set<String> articleIdSet);

    Long countByArticleId(String articleId);

    Long countByUserId(String userId);
}
