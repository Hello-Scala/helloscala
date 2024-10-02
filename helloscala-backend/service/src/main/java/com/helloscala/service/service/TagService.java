package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.Tag;
import com.helloscala.service.web.request.CreateTagRequest;
import com.helloscala.service.web.request.UpdateTagRequest;
import com.helloscala.service.web.view.TagView;

import java.util.List;
import java.util.Set;


public interface TagService extends IService<Tag> {
    Page<TagView> listByName(Page<?> page, String name);

    void createTag(CreateTagRequest request);

    void updateTag(UpdateTagRequest request);

    void deleteTags(Set<String> ids);

    TagView getTagsById(String id);

    List<TagView> listTagByIds(Set<String> idSet);

    List<TagView> listByNames(Set<String> nameSet);

    List<TagView> bulkCreateByNames(Set<String> nameSet);

    List<TagView> listAllTags();

}
