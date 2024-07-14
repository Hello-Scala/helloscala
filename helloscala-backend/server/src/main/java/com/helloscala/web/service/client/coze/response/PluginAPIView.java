package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
class PluginAPIView {
    @JSONField(name = "api_id")
    private String apiId;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "description")
    private String description;
}