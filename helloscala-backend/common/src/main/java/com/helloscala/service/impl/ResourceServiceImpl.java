package com.helloscala.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Resource;
import com.helloscala.mapper.ResourceMapper;
import com.helloscala.service.ResourceService;
import com.helloscala.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    private final ResourceMapper resourceMapper;

    @Override
    public ResponseResult selectResourceList(String type) {
        LambdaQueryWrapper<Resource> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(type)) {
            queryWrapper.eq(Resource::getType,type);
        }
        Page<Resource> resourcePage = resourceMapper.selectPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),queryWrapper);
        return ResponseResult.success(resourcePage);
    }


    @Override
    public  ResponseResult selectResourceById(Integer id) {
        Resource Resource = resourceMapper.selectById(id);
        return ResponseResult.success(Resource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addResource(Resource resource) {
        resourceMapper.insert(resource);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateResource(Resource resource) {
        resourceMapper.updateById(resource);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteResourceByIds(List<Long> ids) {
        resourceMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }
}
