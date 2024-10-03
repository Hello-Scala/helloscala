package com.helloscala.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.utils.ListHelper;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.utils.SetHelper;
import com.helloscala.common.web.exception.GenericException;
import com.helloscala.service.entity.Article;
import com.helloscala.service.entity.Comment;
import com.helloscala.service.entity.User;
import com.helloscala.service.mapper.ArticleMapper;
import com.helloscala.service.mapper.CommentMapper;
import com.helloscala.service.mapper.UserMapper;
import com.helloscala.service.service.CommentService;
import com.helloscala.service.web.request.AddCommentRequest;
import com.helloscala.service.web.request.SearchCommentRequest;
import com.helloscala.service.web.view.CommentView;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    private final ArticleMapper articleMapper;
    private final UserMapper userMapper;
    private final SystemNoticeHandle systemNoticeHandle;

    @Override
    public Page<CommentView> pageByUserId(Page<?> page, String userId) {
        LambdaQueryWrapper<Comment> commentQuery = new LambdaQueryWrapper<>();
        commentQuery.eq(Comment::getUserId, userId)
                .or()
                .eq(Comment::getReplyUserId, userId)
                .orderByDesc(Comment::getCreateTime);
        Page<Comment> commentPage = baseMapper.selectPage(PageHelper.of(page), commentQuery);

        List<Comment> comments = commentPage.getRecords();
        if (ObjectUtil.isEmpty(commentPage)) {
            return PageHelper.of(commentPage);
        }

        Set<String> userIds = comments.stream().map(comment -> List.of(comment.getUserId(), comment.getReplyUserId())).flatMap(Collection::stream).collect(Collectors.toSet());
        Map<String, User> userMap = queryUserMapByIds(userIds);
        Map<String, List<Comment>> childrenMap = listMapByParentIds(comments.stream().map(Comment::getId).collect(Collectors.toSet()));
        Set<String> articleIds = comments.stream().map(Comment::getArticleId).collect(Collectors.toSet());
        Map<String, Article> articleMap = queryArticleByIds(articleIds);

        return PageHelper.convertTo(commentPage, comment -> buildCommentView(userMap, articleMap, childrenMap, comment));
    }

    @Override
    public Page<CommentView> search(Page<?> page, SearchCommentRequest request) {
        Set<String> commentUserIds = new HashSet<>();
        if (ObjectUtil.isNotEmpty(request.getNickNameLike())) {
            Map<String, User> userMap = queryUserMapByNicknameLike(request.getNickNameLike());
            commentUserIds = userMap.keySet();
        }
        if (StrUtil.isNotBlank(request.getUserId())) {
            commentUserIds.add(request.getUserId());
        }

        LambdaQueryWrapper<Comment> commentQuery = new LambdaQueryWrapper<>();
        final Set<String> finalCommentUserIds = commentUserIds;
        commentQuery
                .eq(StrUtil.isNotBlank(request.getArticleId()), Comment::getArticleId, request.getArticleId())
                .and(ObjectUtil.isNotEmpty(finalCommentUserIds), qw -> qw.in(Comment::getUserId, finalCommentUserIds)
                        .or()
                        .in(Comment::getReplyUserId, finalCommentUserIds))
                .isNull(Comment::getParentId)
                .orderByDesc(Comment::getCreateTime);
        Page<Comment> commentPage = baseMapper.selectPage(PageHelper.of(page), commentQuery);

        List<Comment> comments = commentPage.getRecords();
        if (ObjectUtil.isEmpty(commentPage)) {
            return PageHelper.of(commentPage);
        }

        Map<String, List<Comment>> childrenMap = listMapByParentIds(comments.stream().map(Comment::getId).collect(Collectors.toSet()));
        Set<String> userIds = comments.stream().map(comment -> List.of(comment.getUserId(), comment.getReplyUserId())).flatMap(Collection::stream).collect(Collectors.toSet());
        Map<String, User> userMap = queryUserMapByIds(userIds);

        Set<String> articleIds = comments.stream().map(Comment::getArticleId).collect(Collectors.toSet());
        Map<String, Article> articleMap = queryArticleByIds(articleIds);
        return PageHelper.convertTo(commentPage, comment -> buildCommentView(userMap, articleMap, childrenMap, comment));
    }

    private Map<String, List<Comment>> listMapByParentIds(Set<String> parentIds) {
        LambdaQueryWrapper<Comment> commentQuery = new LambdaQueryWrapper<>();
        commentQuery.in(Comment::getParentId, parentIds);
        List<Comment> comments = baseMapper.selectList(commentQuery);
        return ListHelper.ofNullable(comments).stream().collect(Collectors.groupingBy(Comment::getParentId));
    }

    private static CommentView buildCommentView(Map<String, User> userMap, Map<String, Article> articleMap, Map<String, List<Comment>> childrenMap, Comment comment) {
        if (Objects.isNull(comment)) {
            return null;
        }
        User user = userMap.get(comment.getUserId());
        User replyUser = userMap.get(comment.getReplyUserId());
        Article article = articleMap.get(comment.getArticleId());
        List<CommentView> children = childrenMap.getOrDefault(comment.getId(), List.of())
                .stream().map(c -> buildCommentView(userMap, articleMap, childrenMap, c)).toList();

        CommentView commentView = new CommentView();
        commentView.setId(comment.getId());
        if (Objects.nonNull(user)) {
            commentView.setUserId(user.getId());
            commentView.setAvatar(user.getAvatar());
            commentView.setNickname(user.getNickname());
            commentView.setWebSite(user.getWebSite());
        }
        if (Objects.nonNull(replyUser)) {
            commentView.setReplyUserId(replyUser.getId());
            commentView.setReplyNickname(replyUser.getNickname());
            commentView.setReplyWebSite(replyUser.getWebSite());
        }
        if (Objects.nonNull(article)) {
            commentView.setArticleId(article.getId());
            commentView.setArticleTitle(article.getTitle());
        }
        commentView.setContent(comment.getContent());
        commentView.setBrowser(comment.getBrowser());
        commentView.setBrowserVersion(comment.getBrowserVersion());
        commentView.setSystem(comment.getSystem());
        commentView.setSystemVersion(commentView.getSystemVersion());
        commentView.setIpAddress(comment.getIpAddress());
        commentView.setCreateTime(comment.getCreateTime());
        commentView.setChildren(children);
        return commentView;
    }

    private Map<String, Article> queryArticleByIds(Set<String> articleIds) {
        LambdaQueryWrapper<Article> articleQuery = new LambdaQueryWrapper<>();
        articleQuery.select(Article::getId, Article::getTitle)
                .in(Article::getId, articleIds);
        List<Article> articles = articleMapper.selectList(articleQuery);
        return articles.stream().collect(Collectors.toMap(Article::getId, Function.identity()));
    }

    @NotNull
    private Map<String, User> queryUserMapByNicknameLike(String nicknameLike) {
        LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<>();
        userQuery.select(User::getId, User::getAvatar, User::getNickname);
        userQuery.like(StrUtil.isNotBlank(nicknameLike), User::getNickname, nicknameLike);
        List<User> users = userMapper.selectList(userQuery);
        return users.stream().collect(Collectors.toMap(User::getId, Function.identity()));
    }


    @NotNull
    private Map<String, User> queryUserMapByIds(Set<String> ids) {
        if (ObjectUtil.isEmpty(ids)) {
            return Map.of();
        }

        LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<>();
        userQuery.select(User::getId, User::getAvatar, User::getNickname);
        userQuery.in(User::getId, ids);
        List<User> users = userMapper.selectList(userQuery);
        return users.stream().collect(Collectors.toMap(User::getId, Function.identity()));
    }

    @Override
    public void deleteComment(List<String> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public List<CommentView> listArticleComment(Set<String> articleIdSet) {
        if (ObjectUtil.isEmpty(articleIdSet)) {
            return List.of();
        }
        LambdaQueryWrapper<Comment> commentQuery = new LambdaQueryWrapper<Comment>()
                .select(Comment::getId, Comment::getArticleId)
                .in(Comment::getArticleId, articleIdSet);
        List<Comment> comments = baseMapper.selectList(commentQuery);

        Set<String> userIds = comments.stream().map(Comment::getUserId).collect(Collectors.toSet());
        Set<String> replyUserIds = comments.stream().map(Comment::getReplyUserId).collect(Collectors.toSet());
        Set<String> allUserIds = SetHelper.concat(userIds, replyUserIds);
        Map<String, User> userMap = queryUserMapByIds(allUserIds);

        Set<String> articleIds = comments.stream().map(Comment::getArticleId).collect(Collectors.toSet());
        Map<String, Article> articleMap = queryArticleByIds(articleIds);
        Map<String, List<Comment>> childrenMap = listMapByParentIds(comments.stream().map(Comment::getId).collect(Collectors.toSet()));
        return comments.stream().map(comment -> buildCommentView(userMap, articleMap, childrenMap, comment)).toList();
    }

    @Override
    public Long countByArticleId(String articleId) {
        LambdaQueryWrapper<Comment> commentQuery = new LambdaQueryWrapper<>();
        commentQuery.eq(Comment::getArticleId, articleId);
        return baseMapper.selectCount(commentQuery);
    }

    @Override
    public Long countByUserId(String userId) {
        LambdaQueryWrapper<Comment> commentQuery = new LambdaQueryWrapper<>();
        commentQuery.eq(Comment::getUserId, userId);
        return baseMapper.selectCount(commentQuery);
    }

    @Override
    public CommentView addComment(AddCommentRequest request) {
        Comment comment = new Comment();
        comment.setUserId(request.getUserId());
        comment.setReplyUserId(request.getReplyUserId());
        comment.setArticleId(request.getArticleId());
        comment.setContent(request.getContent());
        comment.setParentId(request.getParentId());
        comment.setBrowser(request.getBrowser());
        comment.setBrowserVersion(request.getBrowserVersion());
        comment.setSystem(request.getSystem());
        comment.setSystemVersion(request.getSystemVersion());
        comment.setIpAddress(request.getIpAddress());
        comment.setCreateTime(new Date());

        int insert = baseMapper.insert(comment);
        if (insert == 0) {
            throw new GenericException("Comment failed!");
        }
        String toUserId = comment.getReplyUserId();
        int mark = toUserId == null ? 2 : 1;
        if (toUserId == null) {
            Article article = articleMapper.selectById(comment.getArticleId());
            toUserId = article.getUserId();
        }
        systemNoticeHandle.sendNotice(toUserId, MessageConstant.MESSAGE_COMMENT_NOTICE, MessageConstant.SYSTEM_MESSAGE_CODE, comment.getArticleId(), mark, comment.getContent());

        Set<String> userIds = Set.of(comment.getUserId(), comment.getReplyUserId());
        Map<String, User> userMap = queryUserMapByIds(userIds);
        Map<String, List<Comment>> childrenMap = listMapByParentIds(Set.of(comment.getId()));
        Set<String> articleIds = Set.of(comment.getArticleId());
        Map<String, Article> articleMap = queryArticleByIds(articleIds);
        return buildCommentView(userMap, articleMap, childrenMap, comment);
    }
}
