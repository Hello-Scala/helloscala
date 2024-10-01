package com.helloscala.service.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.web.exception.NotFoundException;
import com.helloscala.service.entity.Resource;
import com.helloscala.service.mapper.ResourceMapper;
import com.helloscala.service.service.ResourceService;
import com.helloscala.service.web.view.CreateResourceRequest;
import com.helloscala.service.web.view.ResourceView;
import com.helloscala.service.web.view.UpdateResourceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    private final ResourceMapper resourceMapper;

    @Override
    public Page<ResourceView> listByPage(Page<?> page, String type) {
        LambdaQueryWrapper<Resource> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(type)) {
            queryWrapper.eq(Resource::getType, type);
        }
        Page<Resource> resourcePage = resourceMapper.selectPage(PageHelper.of(page), queryWrapper);
        return PageHelper.convertTo(resourcePage, ResourceServiceImpl::buildResourceView);
    }

    private static ResourceView buildResourceView(Resource resource) {
        if (Objects.isNull(resource)) {
            return null;
        }
        ResourceView resourceView = new ResourceView();
        resourceView.setId(resource.getId());
        resourceView.setUrl(resource.getUrl());
        resourceView.setType(resource.getType());
        resourceView.setPlatform(resource.getPlatform());
        resourceView.setUserId(resource.getUserId());
        resourceView.setCreateTime(resource.getCreateTime());
        return resourceView;
    }


    @Override
    public ResourceView getResourceById(String id) {
        Resource resource = resourceMapper.selectById(id);
        return buildResourceView(resource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createResource(CreateResourceRequest request) {
        Resource resource = new Resource();
        resource.setUrl(request.getUrl());
        resource.setType(request.getType());
        resource.setPlatform(request.getPlatform());
        resource.setUserId(request.getUserId());
        resource.setCreateTime(new Date());
        resourceMapper.insert(resource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateResource(UpdateResourceRequest request) {
        Resource resource = baseMapper.selectById(request.getId());
        if (Objects.isNull(resource)) {
            throw new NotFoundException("Resource not found, id={}!", request.getId());
        }
        resource.setUrl(request.getUrl());
        resource.setType(request.getType());
        resource.setPlatform(request.getPlatform());
        resource.setUserId(request.getUserId());
        resourceMapper.updateById(resource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteResourceByIds(Set<String> ids) {
        resourceMapper.deleteBatchIds(ids);
    }
}
