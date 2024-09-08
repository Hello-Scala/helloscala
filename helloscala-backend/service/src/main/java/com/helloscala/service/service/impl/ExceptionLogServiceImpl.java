package com.helloscala.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.service.entity.ExceptionLog;
import com.helloscala.service.mapper.ExceptionLogMapper;
import com.helloscala.service.service.ExceptionLogService;
import com.helloscala.service.web.view.ExceptionLogView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
public class ExceptionLogServiceImpl extends ServiceImpl<ExceptionLogMapper, ExceptionLog> implements ExceptionLogService {

    @Override
    public Page<ExceptionLogView> listByPage(Page<?> page) {
        LambdaQueryWrapper<ExceptionLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(ExceptionLog::getCreateTime);
        Page<ExceptionLog> exceptionLogPage = baseMapper.selectPage(PageHelper.of(page), queryWrapper);
        return PageHelper.convertTo(exceptionLogPage, exceptionLog -> {
            ExceptionLogView exceptionLogView = new ExceptionLogView();
            exceptionLogView.setId(exceptionLog.getId());
            exceptionLogView.setUsername(exceptionLog.getUsername());
            exceptionLogView.setIp(exceptionLog.getIp());
            exceptionLogView.setIpSource(exceptionLog.getIpSource());
            exceptionLogView.setMethod(exceptionLog.getMethod());
            exceptionLogView.setOperation(exceptionLog.getOperation());
            exceptionLogView.setParams(exceptionLog.getParams());
            exceptionLogView.setExceptionJson(exceptionLog.getExceptionJson());
            exceptionLogView.setExceptionMessage(exceptionLog.getExceptionMessage());
            exceptionLogView.setCreateTime(exceptionLog.getCreateTime());
            return exceptionLogView;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteExceptionLog(Set<String> ids) {
        baseMapper.deleteBatchIds(ids);
    }
}
