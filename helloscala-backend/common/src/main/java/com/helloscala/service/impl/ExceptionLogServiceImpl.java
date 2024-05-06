package com.helloscala.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.ExceptionLog;
import com.helloscala.mapper.ExceptionLogMapper;
import com.helloscala.service.ExceptionLogService;
import com.helloscala.utils.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ExceptionLogServiceImpl extends ServiceImpl<ExceptionLogMapper, ExceptionLog> implements ExceptionLogService {

    @Override
    public ResponseResult selectExceptionLogPage() {
        Page<ExceptionLog> sysLogPage = baseMapper.selectPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()), new LambdaQueryWrapper<ExceptionLog>()
                .orderByDesc(ExceptionLog::getCreateTime));
        return ResponseResult.success(sysLogPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteExceptionLog(List<Long> ids) {
        baseMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }
}
