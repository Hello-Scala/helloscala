package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Map;

/**
 * @author Steve Zou
 */
@Data
public class ChatView {
    @JSONField(name = "id")
    private String id;

    @JSONField(name = "conversation_id")
    private String conversationId;

    @JSONField(name = "bot_id")
    private String botId;

    @JSONField(name = "created_at")
    private Long createdAt;

    @JSONField(name = "completed_at")
    private Long completedAt;

    @JSONField(name = "failed_at")
    private Long failedAt;

    @JSONField(name = "meta_data")
    private Map<String, String> metaData;

    @JSONField(name = "last_error")
    private CozeError lastError;

    @JSONField(name = "status")
    private ChatStatusEnum status;

    @JSONField(name = "required_action")
    private ActionView requiredAction;

    @JSONField(name = "usage")
    private Usage usage;
}
