package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class ActionView {
    @JSONField(name = "type")
    private ActionTypeEnum type;

    @JSONField(name = "submit_tool_outputs")
    private SubmitToolOutputs submitToolOutputs;
}
