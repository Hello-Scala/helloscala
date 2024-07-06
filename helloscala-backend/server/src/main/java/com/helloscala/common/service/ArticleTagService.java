package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.entity.ArticleTag;

import java.util.List;
import java.util.Set;

public interface ArticleTagService extends IService<ArticleTag> {

    void insertIgnoreArticleTags(Long articleId, Set<Long> tagIdSet);

    void deleteByArticleIds(Set<Long> articleIdSet);

    void resetArticleTags(Long articleId, Set<Long> newTagIds);

    List<ArticleTag> listByArticleIds(Set<Long> articleIds);
}
