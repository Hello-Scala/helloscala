package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.SystemConfig;


public interface SystemConfigService extends IService<SystemConfig> {
    ResponseResult getSystemConfig();

    ResponseResult updateSystemConfig(SystemConfig systemConfig);

    SystemConfig getCustomizeOne();
}
