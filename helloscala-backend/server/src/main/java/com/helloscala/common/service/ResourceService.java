package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Resource;

import java.util.List;

public interface ResourceService extends IService<Resource> {
    ResponseResult selectResourceList(String type);

    ResponseResult selectResourceById(Integer id);

    ResponseResult addResource(Resource resource);

    ResponseResult updateResource(Resource resource);

    ResponseResult deleteResourceByIds(List<Long> ids);
}
