package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.entity.WebConfig;


public interface WebConfigService extends IService<WebConfig> {
    WebConfig getWebConfig();

    void updateWebConfig(WebConfig webConfig);
}
