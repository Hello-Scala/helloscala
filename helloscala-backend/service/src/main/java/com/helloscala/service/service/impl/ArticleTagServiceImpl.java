package com.helloscala.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.service.entity.ArticleTag;
import com.helloscala.service.mapper.ArticleTagMapper;
import com.helloscala.service.service.ArticleTagService;
import com.helloscala.service.web.view.ArticleTagCountView;
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
    public void insertIgnoreArticleTags(String articleId, Set<String> tagIds) {
        baseMapper.insertIgnoreArticleTags(articleId, tagIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByArticleIds(Set<String> articleIds) {
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
    public void resetArticleTags(String articleId, Set<String> newTagIds) {
        deleteByArticleIds(Set.of(articleId));
        insertIgnoreArticleTags(articleId, newTagIds);
    }

    @Override
    public List<ArticleTag> listByArticleIds(Set<String> articleIds) {
        if (ObjectUtil.isEmpty(articleIds)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<ArticleTag> articleTagQuery = new LambdaQueryWrapper<>();
        articleTagQuery.in(ArticleTag::getArticleId, articleIds);
        return baseMapper.selectList(articleTagQuery);
    }

    @Override
    public List<String> listArticleIds(String tagId) {
        if (Objects.isNull(tagId)) {
            return List.of();
        }

        LambdaQueryWrapper<ArticleTag> articleTagQuery = new LambdaQueryWrapper<>();
        articleTagQuery.eq(ArticleTag::getTagId, tagId);
        List<ArticleTag> articleTags = baseMapper.selectList(articleTagQuery);
        return articleTags.stream().map(a -> String.valueOf(a.getArticleId())).toList();
    }

    @Override
    public List<ArticleTagCountView> countByTags(Set<String> tagIds) {
        return baseMapper.countByTagIds(tagIds);
    }
}
