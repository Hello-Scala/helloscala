package com.helloscala.common.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    public Page<Software> selectSoftwareList() {
        Page<Software> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        return baseMapper.selectPage(page,null);
    }

    @Override
    public Software selectSoftwareById(Integer id) {
        return baseMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSoftware(Software Software) {
        baseMapper.insert(Software);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSoftware(Software Software) {
        baseMapper.updateById(Software);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeSoftwareByIds(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
    }
}
