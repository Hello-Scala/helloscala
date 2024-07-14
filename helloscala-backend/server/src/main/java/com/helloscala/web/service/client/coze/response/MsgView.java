package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.helloscala.web.service.client.coze.request.ContentTypeEnum;
import com.helloscala.web.service.client.coze.request.MessageTypeEnum;
import com.helloscala.web.service.client.coze.request.RoleEnum;
import lombok.Data;

import java.util.Map;

/**
 * @author Steve Zou
 */
@Data
public class MsgView {
    @JSONField(name = "id")
    private String id;

    @JSONField(name = "conversation_id")
    private String conversationId;

    @JSONField(name = "bot_id")
    private String botId;

    @JSONField(name = "chat_id")
    private String chatId;

    @JSONField(name = "meta_data")
    private Map<String, Object> metaData;

    @JSONField(name = "role")
    private RoleEnum role;

    @JSONField(name = "content")
    private String content;

    @JSONField(name = "content_type")
    private ContentTypeEnum contentType;

    @JSONField(name = "created_at")
    private long createdAt;

    @JSONField(name = "updated_at")
    private long updatedAt;

    @JSONField(name = "type")
    private MessageTypeEnum type;
}
