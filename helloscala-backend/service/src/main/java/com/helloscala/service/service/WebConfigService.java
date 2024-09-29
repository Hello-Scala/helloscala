package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.WebConfig;
import com.helloscala.service.web.request.UpdateWebConfigRequest;
import com.helloscala.service.web.view.WebConfigView;


public interface WebConfigService extends IService<WebConfig> {
    WebConfigView getWebConfig();

    void updateWebConfig(UpdateWebConfigRequest request);
}
