package com.helloscala.web.service.client.coze.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class UpdateRule {
    @JSONField(name = "update_type")
    private UpdateTypeEnum updateType;

    @JSONField(name = "update_interval")
    private Integer updateInterval;
}
