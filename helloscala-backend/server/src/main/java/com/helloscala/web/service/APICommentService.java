package com.helloscala.web.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.utils.IpUtil;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.service.service.ArticleService;
import com.helloscala.service.service.CommentService;
import com.helloscala.service.web.request.AddCommentRequest;
import com.helloscala.service.web.request.SearchCommentRequest;
import com.helloscala.service.web.view.CommentView;
import com.helloscala.web.controller.comment.APIAddCommentRequest;
import com.helloscala.web.controller.comment.APICommentView;
import com.helloscala.web.utils.HTMLUtil;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class APICommentService {
    private final CommentService commentService;
    private final ArticleService articleService;

    public Page<APICommentView> listByUserId(String userId) {
        Page<?> page = PageUtil.getPage();
        SearchCommentRequest searchCommentRequest = new SearchCommentRequest();
        searchCommentRequest.setUserId(userId);
        Page<CommentView> commentViewPage = commentService.search(page, searchCommentRequest);
        return PageHelper.convertTo(commentViewPage, APICommentService::buildAPICommentView);
    }

    public Page<APICommentView> listByArticleId(String articleId) {
        Page<?> page = PageUtil.getPage();
        SearchCommentRequest searchCommentRequest = new SearchCommentRequest();
        searchCommentRequest.setArticleId(articleId);
        Page<CommentView> commentViewPage = commentService.search(page, searchCommentRequest);
        return PageHelper.convertTo(commentViewPage, APICommentService::buildAPICommentView);
    }

    private static APICommentView buildAPICommentView(CommentView comment) {
        if (Objects.isNull(comment)) {
            return null;
        }
        APICommentView commentView = new APICommentView();
        commentView.setId(comment.getId());
        commentView.setUserId(comment.getUserId());
        commentView.setAvatar(comment.getAvatar());
        commentView.setNickname(comment.getNickname());
        commentView.setWebSite(comment.getWebSite());
        commentView.setReplyUserId(comment.getReplyUserId());
        commentView.setReplyNickname(comment.getReplyNickname());
        commentView.setReplyWebSite(comment.getReplyWebSite());
        commentView.setArticleId(comment.getArticleId());
        commentView.setArticleTitle(comment.getArticleTitle());
        commentView.setContent(comment.getContent());
        commentView.setBrowser(comment.getBrowser());
        commentView.setBrowserVersion(comment.getBrowserVersion());
        commentView.setSystem(comment.getSystem());
        commentView.setSystemVersion(comment.getSystemVersion());
        commentView.setIpAddress(comment.getIpAddress());
        commentView.setCreateTime(comment.getCreateTime());
        commentView.setChildren(comment.getChildren().stream().map(APICommentService::buildAPICommentView).toList());
        return commentView;
    }

    public APICommentView create(String userId, APIAddCommentRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(Objects.requireNonNull(IpUtil.getRequest()).getHeader("user-agent"));
        String ip = IpUtil.getIp();
        String ipAddress = IpUtil.getIp2region(ip);
        String os = userAgent.getOperatingSystem().getName();
        String content = HTMLUtil.deleteTag(request.getContent());
        String system = "android";
        if (os.contains("mac") || os.contains("Mac")) {
            system = "mac";
        } else if (os.contains("Windows")) {
            system = "windowns";
        } else {
            system = "android";
        }
        AddCommentRequest addCommentRequest = new AddCommentRequest();
        addCommentRequest.setUserId(userId);
        addCommentRequest.setReplyUserId(request.getReplyUserId());
        addCommentRequest.setArticleId(request.getArticleId());
        addCommentRequest.setContent(content);
        addCommentRequest.setParentId(request.getParentId());
        addCommentRequest.setBrowser(userAgent.getBrowser().getName());
        addCommentRequest.setBrowserVersion(userAgent.getBrowserVersion().getVersion());
        addCommentRequest.setSystem(system);
        addCommentRequest.setSystemVersion(os);
        addCommentRequest.setIpAddress(ipAddress);
        CommentView commentView = commentService.addComment(addCommentRequest);
        return buildAPICommentView(commentView);
    }
}
