package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class FunctionView {
    @JSONField(name = "name")
    private String name;

    @JSONField(name = "argument")
    private String argument;
}
