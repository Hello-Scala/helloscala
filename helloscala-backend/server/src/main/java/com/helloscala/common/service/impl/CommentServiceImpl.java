package com.helloscala.common.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.entity.Comment;
import com.helloscala.common.mapper.CommentMapper;
import com.helloscala.common.service.CommentService;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.vo.message.SystemCommentVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Override
    public Page<SystemCommentVO> selectCommentPage(String keywords) {
        Page<Object> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        return baseMapper.selectPageList(page, keywords);
    }

    @Override
    public void deleteComment(List<String> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public List<Comment> listArticleComment(Set<String> articleIdSet) {
        if (ObjectUtil.isEmpty(articleIdSet)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<Comment> commentQuery = new LambdaQueryWrapper<Comment>()
                .select(Comment::getId, Comment::getArticleId)
                .in(Comment::getArticleId, articleIdSet);
        return baseMapper.selectList(commentQuery);
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
