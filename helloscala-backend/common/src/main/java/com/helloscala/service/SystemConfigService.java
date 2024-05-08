package com.helloscala.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.SystemConfig;


public interface SystemConfigService extends IService<SystemConfig> {

    /**
     * 获取系统配置
     * @return
     */
    ResponseResult getSystemConfig();

    /**
     * 修改
     * @param systemConfig
     * @return
     */
    ResponseResult updateSystemConfig(SystemConfig systemConfig);

    /**
     *
     * @return
     */
    SystemConfig getCustomizeOne();
}
