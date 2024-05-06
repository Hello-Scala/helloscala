package com.helloscala.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Tags;

import java.util.List;


public interface TagsService extends IService<Tags> {

    /**
     * 分页
     * @param name
     * @return
     */
    ResponseResult selectTagsPage(String name);

    /**
     * 添加
     * @param tags
     * @return
     */
    ResponseResult addTags(Tags tags);

    /**
     * 修改
     * @param tags
     * @return
     */
    ResponseResult updateTag(Tags tags);

    /**
     * 删除
     * @param ids
     * @return
     */
    ResponseResult deleteTags(List<Long> ids);

    /**
     * 详情
     * @param id
     * @return
     */
    ResponseResult getTagsById(Long id);

}
