package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Tags;

import java.util.List;


public interface TagsService extends IService<Tags> {
    ResponseResult selectTagsPage(String name);

    ResponseResult addTags(Tags tags);

    ResponseResult updateTag(Tags tags);

    ResponseResult deleteTags(List<Long> ids);

    ResponseResult getTagsById(Long id);
}
