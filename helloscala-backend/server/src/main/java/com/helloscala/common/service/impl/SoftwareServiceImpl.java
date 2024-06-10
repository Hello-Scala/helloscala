package com.helloscala.common.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Software;
import com.helloscala.common.mapper.SoftwareMapper;
import com.helloscala.common.service.SoftwareService;
import com.helloscala.common.utils.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SoftwareServiceImpl extends ServiceImpl<SoftwareMapper, Software> implements SoftwareService {


    @Override
    public ResponseResult selectSoftwareList() {
        Page<Software> SoftwarePage = baseMapper.selectPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),null);
        return ResponseResult.success(SoftwarePage);
    }


    @Override
    public  ResponseResult selectSoftwareById(Integer id) {
        Software Software = baseMapper.selectById(id);
        return ResponseResult.success(Software);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult saveSoftware(Software Software) {
        baseMapper.insert(Software);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateSoftware(Software Software) {
        baseMapper.updateById(Software);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult removeSoftwareByIds(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }
}
