package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.WebConfig;


public interface WebConfigService extends IService<WebConfig> {
    ResponseResult getWebConfig();

    ResponseResult updateWebConfig(WebConfig webConfig);
}
