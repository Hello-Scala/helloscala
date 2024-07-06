package com.helloscala.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.entity.ExceptionLog;
import com.helloscala.common.mapper.ExceptionLogMapper;
import com.helloscala.common.service.ExceptionLogService;
import com.helloscala.common.utils.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ExceptionLogServiceImpl extends ServiceImpl<ExceptionLogMapper, ExceptionLog> implements ExceptionLogService {

    @Override
    public Page<ExceptionLog> selectExceptionLogPage() {
        Page<ExceptionLog> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        LambdaQueryWrapper<ExceptionLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(ExceptionLog::getCreateTime);
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteExceptionLog(List<Long> ids) {
        baseMapper.deleteBatchIds(ids);
    }
}
