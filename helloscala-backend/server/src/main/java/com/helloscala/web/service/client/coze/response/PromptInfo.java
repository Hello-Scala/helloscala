package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
class PromptInfo {
    @JSONField(name = "prompt")
    private String prompt;
}