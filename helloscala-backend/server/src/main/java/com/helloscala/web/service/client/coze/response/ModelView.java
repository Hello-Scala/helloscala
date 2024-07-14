package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
class ModelView {
    @JSONField(name = "model_id")
    private String modelId;

    @JSONField(name = "model_name")
    private String modelName;
}