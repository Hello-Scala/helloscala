package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.Comment;
import com.helloscala.service.web.view.CommentView;

import java.util.List;
import java.util.Set;


public interface CommentService extends IService<Comment> {
    Page<CommentView> pageByNicknameLike(Page<?> page, String nicknameLike);

    void deleteComment(List<String> ids);

    List<CommentView> listArticleComment(Set<String> articleIdSet);

    Long countByArticleId(String articleId);

    Long countByUserId(String userId);
}
