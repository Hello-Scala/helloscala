package com.helloscala.web.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.entity.Article;
import com.helloscala.common.entity.Comment;
import com.helloscala.common.entity.User;
import com.helloscala.common.mapper.ArticleMapper;
import com.helloscala.common.mapper.CommentMapper;
import com.helloscala.common.mapper.UserMapper;
import com.helloscala.common.utils.IpUtil;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.vo.article.ListArticleVO;
import com.helloscala.common.vo.message.ApiCommentListVO;
import com.helloscala.common.web.exception.GenericException;
import com.helloscala.web.handle.RelativeDateFormat;
import com.helloscala.web.handle.SystemNoticeHandle;
import com.helloscala.web.im.MessageConstant;
import com.helloscala.web.service.ApiCommentService;
import com.helloscala.web.utils.HTMLUtil;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class ApiCommentServiceImpl implements ApiCommentService {
    private final CommentMapper commentMapper;
    private final UserMapper userMapper;
    private final ArticleMapper articleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment addComment(Comment comment) {
        UserAgent userAgent = UserAgent.parseUserAgentString(IpUtil.getRequest().getHeader("user-agent"));
        String ip = IpUtil.getIp();
        String ipAddress = IpUtil.getIp2region(ip);
        String os = userAgent.getOperatingSystem().getName();
        if (os.contains("mac") || os.contains("Mac")) {
            comment.setSystem("mac");
        } else if (os.contains("Windows")) {
            comment.setSystem("windowns");
        } else {
            comment.setSystem("android");
        }
        String content = HTMLUtil.deleteTag(comment.getContent());
        comment.setContent(content);
        comment.setSystemVersion(os);
        comment.setIpAddress(ipAddress);
        comment.setUserId(StpUtil.getLoginIdAsString());
        int insert = commentMapper.insert(comment);
        if (insert == 0) {
            throw new GenericException("Comment failed!");
        }
        String toUserId = comment.getReplyUserId();
        int mark = toUserId == null ? 2 : 1;
        if (toUserId == null) {
            Article article = articleMapper.selectById(comment.getArticleId());
            toUserId = article.getUserId();
        }
        SystemNoticeHandle.sendNotice(toUserId, MessageConstant.MESSAGE_COMMENT_NOTICE, MessageConstant.SYSTEM_MESSAGE_CODE, comment.getArticleId(), mark, comment.getContent());
        return comment;
    }

    @Override
    public Page<ApiCommentListVO> selectCommentByArticleId(Long articleId) {
        Page<ApiCommentListVO> commentListVOPage = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        Page<ApiCommentListVO> pageList = commentMapper.selectCommentPage(commentListVOPage, articleId);

        List<ApiCommentListVO> records = pageList.getRecords();
        Set<Integer> commentIdSet = records.stream().map(ApiCommentListVO::getId).collect(Collectors.toSet());
        List<Comment> commentList = commentMapper.selectList(
                new LambdaQueryWrapper<Comment>().in(Comment::getParentId, commentIdSet).orderByDesc(Comment::getCreateTime));
        Map<Integer, List<Comment>> childCommentMap = commentList.stream().filter(c -> Objects.nonNull(c.getParentId())).collect(Collectors.groupingBy(Comment::getParentId));

        Set<String> replyUserIds = commentList.stream().map(Comment::getReplyUserId).collect(Collectors.toSet());
        Set<String> allUserIdSet = commentList.stream().map(Comment::getUserId).collect(Collectors.toSet());
        allUserIdSet.addAll(replyUserIds);
        List<User> users = userMapper.selectBatchIds(allUserIdSet);
        Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));

        for (ApiCommentListVO vo : records) {
            List<Comment> comments = childCommentMap.getOrDefault(vo.getId(), new ArrayList<>());
            List<ApiCommentListVO> apiCommentListVOS = comments.stream().map(c -> {
                User user = userMap.get(c.getUserId());
                User replyUser = userMap.get(c.getReplyUserId());
                return ApiCommentListVO.builder()
                        .id(c.getId())
                        .userId(c.getUserId())
                        .replyUserId(c.getReplyUserId())
                        .nickname(user.getNickname())
                        .webSite(user.getWebSite())
                        .replyNickname(replyUser.getNickname())
                        .replyWebSite(replyUser.getWebSite())
                        .content(c.getContent())
                        .avatar(user.getAvatar())
                        .createTimeStr(RelativeDateFormat.format(c.getCreateTime()))
                        .browser(c.getBrowser())
                        .browserVersion(c.getBrowserVersion())
                        .system(c.getSystem())
                        .systemVersion(c.getSystemVersion())
                        .ipAddress(c.getIpAddress())
                        .build();
            }).toList();
            vo.setChildren(apiCommentListVOS);
            vo.setCreateTimeStr(RelativeDateFormat.format(vo.getCreateTime()));
        }
        return pageList;
    }

    @Override
    public Page<ListArticleVO> selectMyComment() {
        Page<ListArticleVO> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        String loginId = StpUtil.getLoginIdAsString();
        return commentMapper.selectMyComment(page, loginId);
    }
}
