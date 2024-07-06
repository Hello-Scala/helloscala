package com.helloscala.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.entity.AdminLog;
import com.helloscala.common.mapper.AdminLogMapper;
import com.helloscala.common.service.AdminLogService;
import com.helloscala.common.utils.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class AdminLogServiceImpl extends ServiceImpl<AdminLogMapper, AdminLog> implements AdminLogService {

    @Override
    public Page<AdminLog> selectAdminLogPage() {
        return baseMapper.selectPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),
                new LambdaQueryWrapper<AdminLog>().orderByDesc(AdminLog::getCreateTime));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAdminLog(List<Long> ids) {
        return baseMapper.deleteBatchIds(ids);
    }
}
