package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.entity.Resource;

import java.util.List;

public interface ResourceService extends IService<Resource> {
    Page<Resource> selectResourceList(String type);

    Resource selectResourceById(Integer id);

    void addResource(Resource resource);

    void updateResource(Resource resource);

    void deleteResourceByIds(List<Long> ids);
}
