package com.helloscala.service.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.common.web.exception.NotFoundException;
import com.helloscala.service.entity.SystemConfig;
import com.helloscala.service.mapper.SystemConfigMapper;
import com.helloscala.service.service.SystemConfigService;
import com.helloscala.service.web.request.UpdateSystemConfigRequest;
import com.helloscala.service.web.view.SystemConfigView;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements SystemConfigService {
    @Override
    public SystemConfigView getSystemConfig() {
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
        SystemConfig systemConfig = baseMapper.selectOne(queryWrapper);

        return buildSystemConfigView(systemConfig);
    }

    private static @NotNull SystemConfigView buildSystemConfigView(SystemConfig systemConfig) {
        SystemConfigView systemConfigView = new SystemConfigView();
        systemConfigView.setId(systemConfig.getId());
        systemConfigView.setQiNiuAccessKey(systemConfig.getQiNiuAccessKey());
        systemConfigView.setQiNiuSecretKey(systemConfig.getQiNiuSecretKey());
        systemConfigView.setQiNiuBucket(systemConfig.getQiNiuBucket());
        systemConfigView.setQiNiuArea(systemConfig.getQiNiuArea());
        systemConfigView.setQiNiuPictureBaseUrl(systemConfig.getQiNiuPictureBaseUrl());
        systemConfigView.setStartEmailNotification(systemConfig.getStartEmailNotification());
        systemConfigView.setOpenDashboardNotification(systemConfig.getOpenDashboardNotification());
        systemConfigView.setDashboardNotification(systemConfig.getDashboardNotification());
        systemConfigView.setDashboardNotificationMd(systemConfig.getDashboardNotificationMd());
        systemConfigView.setSearchModel(systemConfig.getSearchModel());
        systemConfigView.setOpenEmailActivate(systemConfig.getOpenEmailActivate());
        systemConfigView.setUploadQiNiu(systemConfig.getUploadQiNiu());
        systemConfigView.setEmailHost(systemConfig.getEmailHost());
        systemConfigView.setEmailUsername(systemConfig.getEmailUsername());
        systemConfigView.setEmailPassword(systemConfig.getEmailPassword());
        systemConfigView.setEmailPort(systemConfig.getEmailPort());
        systemConfigView.setOpenEmail(systemConfig.getOpenEmail());
        systemConfigView.setLocalFileUrl(systemConfig.getLocalFileUrl());
        systemConfigView.setFileUploadWay(systemConfig.getFileUploadWay());
        systemConfigView.setCreateTime(systemConfig.getCreateTime());
        systemConfigView.setUpdateTime(systemConfig.getUpdateTime());
        systemConfigView.setAliYunAccessKey(systemConfig.getAliYunAccessKey());
        systemConfigView.setAliYunSecretKey(systemConfig.getAliYunSecretKey());
        systemConfigView.setAliYunBucket(systemConfig.getAliYunBucket());
        systemConfigView.setAliYunEndpoint(systemConfig.getAliYunEndpoint());
        return systemConfigView;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSystemConfig(UpdateSystemConfigRequest request) {
        SystemConfig systemConfig = baseMapper.selectById(request.getId());
        if (Objects.isNull(systemConfig)) {
            throw new NotFoundException("SystemConfig not found, id={}!", systemConfig);
        }
        systemConfig.setQiNiuAccessKey(request.getQiNiuAccessKey());
        systemConfig.setQiNiuSecretKey(request.getQiNiuSecretKey());
        systemConfig.setQiNiuBucket(request.getQiNiuBucket());
        systemConfig.setQiNiuArea(request.getQiNiuArea());
        systemConfig.setQiNiuPictureBaseUrl(request.getQiNiuPictureBaseUrl());
        systemConfig.setStartEmailNotification(request.getStartEmailNotification());
        systemConfig.setOpenDashboardNotification(request.getOpenDashboardNotification());
        systemConfig.setDashboardNotification(request.getDashboardNotification());
        systemConfig.setDashboardNotificationMd(request.getDashboardNotificationMd());
        systemConfig.setSearchModel(request.getSearchModel());
        systemConfig.setOpenEmailActivate(request.getOpenEmailActivate());
        systemConfig.setUploadQiNiu(request.getUploadQiNiu());
        systemConfig.setEmailHost(request.getEmailHost());
        systemConfig.setEmailUsername(request.getEmailUsername());
        systemConfig.setEmailPassword(request.getEmailPassword());
        systemConfig.setEmailPort(request.getEmailPort());
        systemConfig.setOpenEmail(request.getOpenEmail());
        systemConfig.setLocalFileUrl(request.getLocalFileUrl());
        systemConfig.setFileUploadWay(request.getFileUploadWay());
        systemConfig.setCreateTime(request.getCreateTime());
        systemConfig.setUpdateTime(request.getUpdateTime());
        systemConfig.setAliYunAccessKey(request.getAliYunAccessKey());
        systemConfig.setAliYunSecretKey(request.getAliYunSecretKey());
        systemConfig.setAliYunBucket(request.getAliYunBucket());
        systemConfig.setAliYunEndpoint(request.getAliYunEndpoint());
        baseMapper.updateById(systemConfig);
    }

    @Override
    public SystemConfigView getCustomizeOne() {
        LambdaQueryWrapper<SystemConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(SystemConfig::getCreateTime);
        queryWrapper.last(Constants.LIMIT_ONE);
        SystemConfig systemConfig = baseMapper.selectOne(queryWrapper);
        return buildSystemConfigView(systemConfig);
    }
}
