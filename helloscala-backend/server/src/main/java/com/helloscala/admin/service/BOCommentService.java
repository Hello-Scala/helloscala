package com.helloscala.admin.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.view.BOCommentView;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.ResponseHelper;
import com.helloscala.service.service.CommentService;
import com.helloscala.service.web.request.SearchCommentRequest;
import com.helloscala.service.web.view.CommentView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Steve Zou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOCommentService {
    private final CommentService commentService;

    public Page<BOCommentView> pageByNicknameLike(String nicknameLike) {
        Page<?> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        SearchCommentRequest searchCommentRequest = new SearchCommentRequest();
        searchCommentRequest.setNickNameLike(nicknameLike);
        Page<CommentView> commentViewPage = commentService.search(page, searchCommentRequest);
        return PageHelper.convertTo(commentViewPage, comment -> {
            BOCommentView boCommentView = new BOCommentView();
            boCommentView.setId(comment.getId());
            boCommentView.setAvatar(comment.getAvatar());
            boCommentView.setNickname(comment.getNickname());
            boCommentView.setReplyNickname(comment.getReplyNickname());
            boCommentView.setArticleTitle(comment.getArticleTitle());
            boCommentView.setContent(comment.getContent());
            boCommentView.setCreateTime(comment.getCreateTime());
            return boCommentView;
        });
    }

    public void deleteBatch(String userId, List<String> ids) {
        commentService.deleteComment(ids);
        log.info("deleteBatch success, userId={}, ids=[{}]", userId, StrUtil.join(",", ids));
    }
}
