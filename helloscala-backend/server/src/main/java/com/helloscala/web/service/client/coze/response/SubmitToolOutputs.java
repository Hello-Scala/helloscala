package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class SubmitToolOutputs {
    @JSONField(name = "tool_calls")
    private List<ToolCall> toolCalls;
}