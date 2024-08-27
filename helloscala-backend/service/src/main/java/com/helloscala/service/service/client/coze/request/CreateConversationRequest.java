package com.helloscala.web.service.client.coze.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Steve Zou
 */
@Data
public class CreateConversationRequest {
    @JSONField(name = "messages")
    private List<ConversationMsgView> messages;

    @JSONField(name = "meta_data")
    private Map<String, String> metaData;
}
