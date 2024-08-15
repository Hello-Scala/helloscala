package com.helloscala.common.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.entity.ArticleTag;
import com.helloscala.common.mapper.ArticleTagMapper;
import com.helloscala.common.service.ArticleTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Steve Zou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertIgnoreArticleTags(Long articleId, Set<Long> tagIds) {
        baseMapper.insertIgnoreArticleTags(articleId, tagIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByArticleIds(Set<Long> articleIds) {
        if (ObjectUtil.isEmpty(articleIds)) {
            log.warn("not tags to delete, since articleIds is empty!");
            return;
        }
        LambdaQueryWrapper<ArticleTag> articleTagQuery = new LambdaQueryWrapper<>();
        articleTagQuery.in(ArticleTag::getArticleId, articleIds);
        baseMapper.delete(articleTagQuery);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetArticleTags(Long articleId, Set<Long> newTagIds) {
        deleteByArticleIds(Set.of(articleId));
        insertIgnoreArticleTags(articleId, newTagIds);
    }

    @Override
    public List<ArticleTag> listByArticleIds(Set<Long> articleIds) {
        if (ObjectUtil.isEmpty(articleIds)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<ArticleTag> articleTagQuery = new LambdaQueryWrapper<>();
        articleTagQuery.in(ArticleTag::getArticleId, articleIds);
        return baseMapper.selectList(articleTagQuery);
    }

    @Override
    public List<String> listArticleIds(Long tagId) {
        if (Objects.isNull(tagId)) {
            return List.of();
        }

        LambdaQueryWrapper<ArticleTag> articleTagQuery = new LambdaQueryWrapper<>();
        articleTagQuery.eq(ArticleTag::getTagId, tagId);
        List<ArticleTag> articleTags = baseMapper.selectList(articleTagQuery);
        return articleTags.stream().map(a -> String.valueOf(a.getArticleId())).toList();
    }
}
