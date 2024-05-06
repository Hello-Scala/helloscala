package com.helloscala.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.WebConfig;


public interface WebConfigService extends IService<WebConfig> {

    /**
     * 获取网站配置详情
     * @return
     */
    ResponseResult getWebConfig();

    /**
     * 修改
     * @param webConfig
     * @return
     */
    ResponseResult updateWebConfig(WebConfig webConfig);
}
