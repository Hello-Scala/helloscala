package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class ToolOutput {
    @JSONField(name = "tool_call_id")
    private String toolCallId;

    @JSONField(name = "output")
    private String output;
}
