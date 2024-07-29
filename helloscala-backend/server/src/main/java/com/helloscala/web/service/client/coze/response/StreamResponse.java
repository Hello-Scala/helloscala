package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class StreamResponse<T> {
    @JSONField(name = "event")
    private StreamEventEnum event;

    @JSONField(name = "data")
    private T data;
}
