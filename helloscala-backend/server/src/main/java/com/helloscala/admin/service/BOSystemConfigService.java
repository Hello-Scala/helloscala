package com.helloscala.admin.service;

import com.helloscala.admin.controller.request.BOUpdateSystemConfigRequest;
import com.helloscala.admin.controller.view.BOSystemConfigView;
import com.helloscala.service.service.SystemConfigService;
import com.helloscala.service.web.request.UpdateSystemConfigRequest;
import com.helloscala.service.web.view.SystemConfigView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author stevezou
 */
@Service
@RequiredArgsConstructor
public class BOSystemConfigService {
    private final SystemConfigService systemConfigService;

    public BOSystemConfigView getSystemConfig() {
        SystemConfigView systemConfig = systemConfigService.getSystemConfig();

        BOSystemConfigView systemConfigView = new BOSystemConfigView();
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

    public void updateSystemConfig(String userId, BOUpdateSystemConfigRequest request) {
        UpdateSystemConfigRequest updateSystemConfigRequest = new UpdateSystemConfigRequest();
        updateSystemConfigRequest.setId(request.getId());
        updateSystemConfigRequest.setQiNiuAccessKey(request.getQiNiuAccessKey());
        updateSystemConfigRequest.setQiNiuSecretKey(request.getQiNiuSecretKey());
        updateSystemConfigRequest.setQiNiuBucket(request.getQiNiuBucket());
        updateSystemConfigRequest.setQiNiuArea(request.getQiNiuArea());
        updateSystemConfigRequest.setQiNiuPictureBaseUrl(request.getQiNiuPictureBaseUrl());
        updateSystemConfigRequest.setStartEmailNotification(request.getStartEmailNotification());
        updateSystemConfigRequest.setOpenDashboardNotification(request.getOpenDashboardNotification());
        updateSystemConfigRequest.setDashboardNotification(request.getDashboardNotification());
        updateSystemConfigRequest.setDashboardNotificationMd(request.getDashboardNotificationMd());
        updateSystemConfigRequest.setSearchModel(request.getSearchModel());
        updateSystemConfigRequest.setOpenEmailActivate(request.getOpenEmailActivate());
        updateSystemConfigRequest.setUploadQiNiu(request.getUploadQiNiu());
        updateSystemConfigRequest.setEmailHost(request.getEmailHost());
        updateSystemConfigRequest.setEmailUsername(request.getEmailUsername());
        updateSystemConfigRequest.setEmailPassword(request.getEmailPassword());
        updateSystemConfigRequest.setEmailPort(request.getEmailPort());
        updateSystemConfigRequest.setOpenEmail(request.getOpenEmail());
        updateSystemConfigRequest.setLocalFileUrl(request.getLocalFileUrl());
        updateSystemConfigRequest.setFileUploadWay(request.getFileUploadWay());
        updateSystemConfigRequest.setCreateTime(request.getCreateTime());
        updateSystemConfigRequest.setUpdateTime(request.getUpdateTime());
        updateSystemConfigRequest.setAliYunAccessKey(request.getAliYunAccessKey());
        updateSystemConfigRequest.setAliYunSecretKey(request.getAliYunSecretKey());
        updateSystemConfigRequest.setAliYunBucket(request.getAliYunBucket());
        updateSystemConfigRequest.setAliYunEndpoint(request.getAliYunEndpoint());
        updateSystemConfigRequest.setRequestBy(userId);
        systemConfigService.updateSystemConfig(updateSystemConfigRequest);
    }
}
