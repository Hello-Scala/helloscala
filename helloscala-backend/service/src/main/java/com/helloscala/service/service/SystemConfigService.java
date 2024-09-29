package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.SystemConfig;
import com.helloscala.service.web.request.UpdateSystemConfigRequest;
import com.helloscala.service.web.view.SystemConfigView;


public interface SystemConfigService extends IService<SystemConfig> {
    SystemConfigView getSystemConfig();

    void updateSystemConfig(UpdateSystemConfigRequest request);

    SystemConfigView getCustomizeOne();
}
