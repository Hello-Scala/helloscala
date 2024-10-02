package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.ArticleTag;
import com.helloscala.service.web.view.ArticleTagCountView;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ArticleTagService extends IService<ArticleTag> {

    void insertIgnoreArticleTags(String articleId, Set<String> tagIdSet);

    void deleteByArticleIds(Set<String> articleIdSet);

    void resetArticleTags(String articleId, Set<String> newTagIds);

    List<ArticleTag> listByArticleIds(Set<String> articleIds);

    List<String> listArticleIds(String tagId);

    List<ArticleTagCountView> countByTags(Set<String> tagIds);
}
