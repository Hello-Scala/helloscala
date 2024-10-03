package com.helloscala.web.service;

import cn.hutool.core.util.ObjectUtil;
import com.helloscala.service.entity.ArticleTag;
import com.helloscala.service.service.ArticleTagService;
import com.helloscala.service.service.TagService;
import com.helloscala.service.web.view.TagView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class APIArticleTagService {
    private final TagService tagService;
    private final ArticleTagService articleTagService;

    @NotNull
    public Map<String, List<TagView>> getArticleTagListMap(Set<String> articleIdSet) {
        if (ObjectUtil.isEmpty(articleIdSet)) {
            return Map.of();
        }
        List<ArticleTag> articleTags = articleTagService.listByArticleIds(articleIdSet);
        Map<String, List<ArticleTag>> articleTagMap = articleTags.stream().collect(Collectors.groupingBy(ArticleTag::getArticleId));

        Set<String> tagIdSet = articleTags.stream().map(ArticleTag::getTagId).collect(Collectors.toSet());
        List<TagView> tags = tagService.listTagByIds(tagIdSet);
        Map<String, TagView> tagMap = tags.stream().collect(Collectors.toMap(TagView::getId, Function.identity()));
        return articleTagMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().map(at -> tagMap.get(at.getTagId())).filter(Objects::nonNull).toList()));
    }
}
