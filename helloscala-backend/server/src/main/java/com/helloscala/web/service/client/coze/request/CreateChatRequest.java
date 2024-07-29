package com.helloscala.web.service.client.coze.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * @author Steve Zou
 */
@Data
public class CreateChatRequest {
    @JSONField(name = "bot_id")
    private String botId;

    @JSONField(name = "user_id")
    private String userId;

    @JSONField(name = "additional_messages")
    private List<ConversationMsgView> additionalMsgs;

    @JSONField(name = "stream")
    private Boolean stream;

    @JSONField(name = "custom_variables")
    private Map<String, String> customVariables;

    @JSONField(name = "auto_save_history")
    private Boolean autoSaveHistory;

    @JSONField(name = "meta_data")
    private Map<String, String> metaData;
}
