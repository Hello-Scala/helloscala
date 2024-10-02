package com.helloscala.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.request.BOCreateTagRequest;
import com.helloscala.admin.controller.request.BOUpdateTagRequest;
import com.helloscala.admin.controller.view.BOListTagView;
import com.helloscala.admin.controller.view.BOTagView;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.service.service.ArticleTagService;
import com.helloscala.service.service.TagService;
import com.helloscala.service.web.request.CreateTagRequest;
import com.helloscala.service.web.request.UpdateTagRequest;
import com.helloscala.service.web.view.ArticleTagCountView;
import com.helloscala.service.web.view.TagView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BOTagService {
    private final TagService tagService;
    private final ArticleTagService articleTagService;

    public Page<BOListTagView> listByName(String name) {
        Page<?> page = PageUtil.getPage();
        Page<TagView> tagPage = tagService.listByName(page, name);

        Set<String> tagIds = tagPage.getRecords().stream().map(TagView::getId).collect(Collectors.toSet());
        List<ArticleTagCountView> articleTagCountViews = articleTagService.countByTags(tagIds);
        Map<String, ArticleTagCountView> articleTagCountViewMap = articleTagCountViews.stream().collect(Collectors.toMap(ArticleTagCountView::getTagId, Function.identity()));
        return PageHelper.convertTo(tagPage, tag -> {
            Integer count = Optional.ofNullable(articleTagCountViewMap.get(tag.getId())).map(ArticleTagCountView::getCount).orElse(0);

            BOListTagView listTagView = new BOListTagView();
            listTagView.setId(tag.getId());
            listTagView.setName(tag.getName());
            listTagView.setSort(tag.getSort());
            listTagView.setClickVolume(tag.getClickVolume());
            listTagView.setCreateTime(tag.getCreateTime());
            listTagView.setUpdateTime(tag.getUpdateTime());
            listTagView.setArticleCount(count);
            return listTagView;
        });
    }

    public void create(String userId, BOCreateTagRequest request) {
        CreateTagRequest createTagRequest = new CreateTagRequest();
        createTagRequest.setName(request.getName());
        createTagRequest.setSort(request.getSort());
        createTagRequest.setRequestBy(userId);
        tagService.createTag(createTagRequest);
    }

    public void update(String userId, BOUpdateTagRequest request) {
        UpdateTagRequest updateTagRequest = new UpdateTagRequest();
        updateTagRequest.setId(request.getId());
        updateTagRequest.setName(request.getName());
        updateTagRequest.setSort(request.getSort());
        updateTagRequest.setRequestBy(userId);
        tagService.updateTag(updateTagRequest);
    }

    public BOTagView get(String id) {
        TagView tag = tagService.getTagsById(id);
        BOTagView listTagView = new BOTagView();
        listTagView.setId(tag.getId());
        listTagView.setName(tag.getName());
        listTagView.setSort(tag.getSort());
        listTagView.setClickVolume(tag.getClickVolume());
        listTagView.setCreateTime(tag.getCreateTime());
        listTagView.setUpdateTime(tag.getUpdateTime());
        return listTagView;
    }

    public void bulkDelete(String userId, Set<String> ids) {
        tagService.deleteTags(ids);
        log.info("userId={}, deleted Tag ids=[{}]", userId, String.join(",", ids));
    }
}
