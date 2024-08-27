package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class PromptInfo {
    @JSONField(name = "prompt")
    private String prompt;
}