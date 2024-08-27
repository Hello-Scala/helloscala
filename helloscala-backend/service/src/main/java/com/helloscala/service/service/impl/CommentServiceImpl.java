package com.helloscala.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.utils.SetHelper;
import com.helloscala.service.entity.Article;
import com.helloscala.service.entity.Comment;
import com.helloscala.service.entity.User;
import com.helloscala.service.mapper.ArticleMapper;
import com.helloscala.service.mapper.CommentMapper;
import com.helloscala.service.mapper.UserMapper;
import com.helloscala.service.service.CommentService;
import com.helloscala.service.web.view.CommentView;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    private final ArticleMapper articleMapper;
    private final UserMapper userMapper;

    @Override
    public Page<CommentView> pageByNicknameLike(Page<?> page, String keywords) {

        Map<String, User> userMap = queryUserMapByNicknameLike(keywords);
        Set<String> userIds = userMap.keySet();

        LambdaQueryWrapper<Comment> commentQuery = new LambdaQueryWrapper<>();
        commentQuery.in(Comment::getUserId, userIds)
                .or()
                .in(Comment::getReplyUserId, userIds)
                .orderByDesc(Comment::getCreateTime);
        Page<Comment> commentPage = baseMapper.selectPage(PageHelper.of(page), commentQuery);

        List<Comment> comments = commentPage.getRecords();
        if (ObjectUtil.isEmpty(commentPage)) {
            return PageHelper.of(commentPage);
        }

        Set<String> articleIds = comments.stream().map(Comment::getArticleId).collect(Collectors.toSet());
        Map<String, Article> articleMap = queryArticleByIds(articleIds);
        return PageHelper.convertTo(commentPage, mapComment(userMap, articleMap));
    }

    @NotNull
    private static Function<Comment, CommentView> mapComment(Map<String, User> userMap, Map<String, Article> articleMap) {
        return comment -> {
            User user = userMap.get(comment.getUserId());
            User replyUser = userMap.get(comment.getReplyUserId());
            Article article = articleMap.get(comment.getArticleId());

            CommentView commentView = new CommentView();
            commentView.setId(comment.getId());
            if (Objects.nonNull(user)) {
                commentView.setAvatar(user.getAvatar());
                commentView.setNickname(user.getNickname());
            }
            if (Objects.nonNull(replyUser)) {
                commentView.setReplyNickname(replyUser.getNickname());
            }
            if (Objects.nonNull(article)) {
                commentView.setArticleId(article.getId());
                commentView.setArticleTitle(article.getTitle());
            }
            commentView.setContent(comment.getContent());
            commentView.setCreateTime(comment.getCreateTime());
            return commentView;
        };
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
        return comments.stream().map(mapComment(userMap, articleMap)).toList();
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
}
