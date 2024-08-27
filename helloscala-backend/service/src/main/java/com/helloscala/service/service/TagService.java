package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.Tag;
import com.helloscala.common.vo.tag.SystemTagListVo;

import java.util.List;
import java.util.Set;


public interface TagService extends IService<Tag> {
    Page<SystemTagListVo> selectByName(String name);

    void addTags(Tag tags);

    void updateTag(Tag tags);

    void deleteTags(List<Long> ids);

    Tag getTagsById(Long id);

    List<Tag> listByIds(Set<Long> idSet);

    List<Tag> listByNames(Set<String> nameSet);

    List<Tag> bulkCreateByNames(Set<String> nameSet);
}
