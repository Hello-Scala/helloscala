package com.helloscala.service.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.service.entity.SystemConfig;
import com.helloscala.service.mapper.SystemConfigMapper;
import com.helloscala.service.service.SystemConfigService;
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
