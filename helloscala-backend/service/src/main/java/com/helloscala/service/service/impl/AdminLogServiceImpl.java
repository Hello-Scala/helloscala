package com.helloscala.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.service.entity.AdminLog;
import com.helloscala.service.mapper.AdminLogMapper;
import com.helloscala.service.service.AdminLogService;
import com.helloscala.service.web.view.AdminLogView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class AdminLogServiceImpl extends ServiceImpl<AdminLogMapper, AdminLog> implements AdminLogService {

    @Override
    public Page<AdminLogView> listByPage(Page<?> page) {
        PageHelper.of(page);
        LambdaQueryWrapper<AdminLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(AdminLog::getCreateTime);

        Page<AdminLog> adminLogPage = baseMapper.selectPage(PageHelper.of(page), queryWrapper);
        return PageHelper.convertTo(adminLogPage, adminLog -> {
            AdminLogView adminLogView = new AdminLogView();
            adminLogView.setId(adminLog.getId());
            adminLogView.setUsername(adminLog.getUsername());
            adminLogView.setRequestUrl(adminLog.getRequestUrl());
            adminLogView.setType(adminLog.getType());
            adminLogView.setOperationName(adminLog.getOperationName());
            adminLogView.setIp(adminLog.getIp());
            adminLogView.setSource(adminLog.getSource());
            adminLogView.setParamsJson(adminLog.getParamsJson());
            adminLogView.setClassPath(adminLog.getClassPath());
            adminLogView.setMethodName(adminLog.getMethodName());
            adminLogView.setSpendTime(adminLog.getSpendTime());
            adminLogView.setCreateTime(adminLog.getCreateTime());
            return adminLogView;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAdminLog(List<Long> ids) {
        return baseMapper.deleteBatchIds(ids);
    }
}
