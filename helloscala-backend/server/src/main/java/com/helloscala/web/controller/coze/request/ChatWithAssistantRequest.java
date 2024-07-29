package com.helloscala.web.controller.coze.request;

import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class ChatWithAssistantRequest {
    private String conversationId;

    private MessageView msg;
}
