package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class CozeResponse<T> {
    @JSONField(name = "code")
    private String code;

    @JSONField(name = "msg")
    private int msg;

    @JSONField(name = "data")
    private T data;
}
