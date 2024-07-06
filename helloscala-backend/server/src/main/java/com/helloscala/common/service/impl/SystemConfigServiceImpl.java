package com.helloscala.common.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.common.entity.SystemConfig;
import com.helloscala.common.mapper.SystemConfigMapper;
import com.helloscala.common.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements SystemConfigService {
    @Override
    public SystemConfig getSystemConfig() {
        LambdaQueryWrapper<SystemConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.last(Constants.LIMIT_ONE);

        if (!StpUtil.hasRole(Constants.ADMIN_CODE)) {
            queryWrapper.select(SystemConfig::getId, SystemConfig::getOpenEmail, SystemConfig::getOpenEmailActivate, SystemConfig::getStartEmailNotification,
                    SystemConfig::getFileUploadWay, SystemConfig::getDashboardNotification, SystemConfig::getDashboardNotificationMd,
                    SystemConfig::getOpenDashboardNotification, SystemConfig::getSearchModel, SystemConfig::getLocalFileUrl,
                    SystemConfig::getEmailHost, SystemConfig::getEmailPort);
        }
        queryWrapper.orderByDesc(SystemConfig::getCreateTime);
        queryWrapper.last(Constants.LIMIT_ONE);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSystemConfig(SystemConfig systemConfig) {
        baseMapper.updateById(systemConfig);
    }

    @Override
    public SystemConfig getCustomizeOne() {
        LambdaQueryWrapper<SystemConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(SystemConfig::getCreateTime);
        queryWrapper.last(Constants.LIMIT_ONE);
        return baseMapper.selectOne(queryWrapper);
    }
}
