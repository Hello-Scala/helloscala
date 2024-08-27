package com.helloscala.service.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.service.entity.Resource;
import com.helloscala.service.mapper.ResourceMapper;
import com.helloscala.service.service.ResourceService;
import com.helloscala.common.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    private final ResourceMapper resourceMapper;

    @Override
    public Page<Resource> selectResourceList(String type) {
        LambdaQueryWrapper<Resource> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(type)) {
            queryWrapper.eq(Resource::getType,type);
        }
        Page<Resource> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        return resourceMapper.selectPage(page,queryWrapper);
    }


    @Override
    public Resource selectResourceById(Integer id) {
        return resourceMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addResource(Resource resource) {
        resourceMapper.insert(resource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateResource(Resource resource) {
        resourceMapper.updateById(resource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteResourceByIds(List<Long> ids) {
        resourceMapper.deleteBatchIds(ids);
    }
}
