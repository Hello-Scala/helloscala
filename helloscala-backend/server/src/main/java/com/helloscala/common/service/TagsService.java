package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.entity.Tag;
import com.helloscala.common.vo.tag.SystemTagListVo;

import java.util.List;


public interface TagsService extends IService<Tag> {
    Page<SystemTagListVo> selectByName(String name);

    void addTags(Tag tags);

    void updateTag(Tag tags);

    void deleteTags(List<Long> ids);

    Tag getTagsById(Long id);
}
