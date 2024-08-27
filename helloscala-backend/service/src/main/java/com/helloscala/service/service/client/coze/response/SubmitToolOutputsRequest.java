package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author Steve Zou
 */
@Data
public class SubmitToolOutputsRequest {
    @JSONField(name = "tool_outputs")
    private List<ToolOutput> toolOutputs;

    @JSONField(name = "stream")
    private boolean stream;
}
