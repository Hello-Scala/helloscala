package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.entity.ArticleTag;

import java.util.List;
import java.util.Set;

public interface ArticleTagService extends IService<ArticleTag> {

    void insertIgnoreArticleTags(String articleId, Set<String> tagIdSet);

    void deleteByArticleIds(Set<String> articleIdSet);

    void resetArticleTags(String articleId, Set<String> newTagIds);

    List<ArticleTag> listByArticleIds(Set<String> articleIds);

    List<String> listArticleIds(String tagId);
}
