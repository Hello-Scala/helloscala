package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class StreamEvent<T> {
    @JSONField(name = "event")
    private String event;

    @JSONField(name = "data")
    private T data;
}
