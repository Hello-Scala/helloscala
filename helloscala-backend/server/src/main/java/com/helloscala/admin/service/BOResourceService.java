package com.helloscala.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.request.BOCreateResourceRequest;
import com.helloscala.admin.controller.request.BOUpdateResourceRequest;
import com.helloscala.admin.controller.view.BOResourceView;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.service.service.ResourceService;
import com.helloscala.service.web.view.CreateResourceRequest;
import com.helloscala.service.web.view.ResourceView;
import com.helloscala.service.web.view.UpdateResourceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BOResourceService {
    private final ResourceService resourceService;

    public Page<BOResourceView> listByPage(String type) {
        Page<?> page = PageUtil.getPage();
        Page<ResourceView> resourceViewPage = resourceService.listByPage(page, type);
        return PageHelper.convertTo(resourceViewPage, BOResourceService::buildBOResourceView);
    }

    private static @NotNull BOResourceView buildBOResourceView(ResourceView resource) {
        BOResourceView resourceView = new BOResourceView();
        resourceView.setId(resource.getId());
        resourceView.setUrl(resource.getUrl());
        resourceView.setType(resource.getType());
        resourceView.setPlatform(resource.getPlatform());
        resourceView.setUserId(resource.getUserId());
        resourceView.setCreateTime(resource.getCreateTime());
        return resourceView;
    }

    public BOResourceView get(String id) {
        ResourceView resource = resourceService.getResourceById(id);
        return buildBOResourceView(resource);
    }

    public void create(String userId, BOCreateResourceRequest request) {
        CreateResourceRequest createResourceRequest = new CreateResourceRequest();
        createResourceRequest.setUrl(request.getUrl());
        createResourceRequest.setType(request.getType());
        createResourceRequest.setPlatform(request.getPlatform());
        createResourceRequest.setUserId(request.getUserId());
        createResourceRequest.setRequestBy(userId);
        resourceService.createResource(createResourceRequest);
    }

    public void update(String userId, BOUpdateResourceRequest request) {
        UpdateResourceRequest updateResourceRequest = new UpdateResourceRequest();
        updateResourceRequest.setId(request.getId());
        updateResourceRequest.setUrl(request.getUrl());
        updateResourceRequest.setType(request.getType());
        updateResourceRequest.setPlatform(request.getPlatform());
        updateResourceRequest.setUserId(request.getUserId());
        updateResourceRequest.setRequestBy(userId);
        resourceService.updateResource(updateResourceRequest);
    }

    public void bulkDelete(String userId, Set<String> ids) {
        resourceService.deleteResourceByIds(ids);
        log.info("userId={}, deleted Resource ids=[{}]", userId, String.join(",", ids));
    }
}
