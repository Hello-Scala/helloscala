package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.SystemConfig;


public interface SystemConfigService extends IService<SystemConfig> {
    SystemConfig getSystemConfig();

    void updateSystemConfig(SystemConfig systemConfig);

    SystemConfig getCustomizeOne();
}
