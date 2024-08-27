package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.WebConfig;


public interface WebConfigService extends IService<WebConfig> {
    WebConfig getWebConfig();

    void updateWebConfig(WebConfig webConfig);
}
