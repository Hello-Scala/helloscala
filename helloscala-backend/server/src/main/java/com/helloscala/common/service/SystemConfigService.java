package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.entity.SystemConfig;


public interface SystemConfigService extends IService<SystemConfig> {
    SystemConfig getSystemConfig();

    void updateSystemConfig(SystemConfig systemConfig);

    SystemConfig getCustomizeOne();
}
