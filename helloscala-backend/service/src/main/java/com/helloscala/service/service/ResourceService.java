package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.Resource;
import com.helloscala.service.web.view.CreateResourceRequest;
import com.helloscala.service.web.view.ResourceView;
import com.helloscala.service.web.view.UpdateResourceRequest;

import java.util.List;
import java.util.Set;

public interface ResourceService extends IService<Resource> {
    Page<ResourceView> listByPage(Page<?> page, String type);

    ResourceView getResourceById(String id);

    void createResource(CreateResourceRequest request);

    void updateResource(UpdateResourceRequest request);

    void deleteResourceByIds(Set<String> ids);
}
