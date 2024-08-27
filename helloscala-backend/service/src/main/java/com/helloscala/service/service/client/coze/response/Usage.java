package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class Usage {
    @JSONField(name = "token_count")
    private Integer tokenCount;

    @JSONField(name = "output_count")
    private Integer outputCount;

    @JSONField(name = "input_count")
    private Integer inputCount;
}