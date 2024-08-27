package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class CozeError {
    @JSONField(name = "code")
    private Integer code;

    @JSONField(name = "msg")
    private String msg;
}
