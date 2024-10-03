package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.Comment;
import com.helloscala.service.web.request.AddCommentRequest;
import com.helloscala.service.web.request.SearchCommentRequest;
import com.helloscala.service.web.view.CommentView;

import java.util.List;
import java.util.Set;


public interface CommentService extends IService<Comment> {

    Page<CommentView> pageByUserId(Page<?> page, String userId);

    Page<CommentView> search(Page<?> page, SearchCommentRequest request);

    void deleteComment(List<String> ids);

    List<CommentView> listArticleComment(Set<String> articleIdSet);

    Long countByArticleId(String articleId);

    Long countByUserId(String userId);

    CommentView addComment(AddCommentRequest request);
}
