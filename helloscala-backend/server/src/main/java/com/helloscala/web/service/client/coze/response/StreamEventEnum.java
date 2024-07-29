package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.helloscala.common.web.exception.ConflictException;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Steve Zou
 */
@Getter
public enum StreamEventEnum {
    CHAT_CREATED("conversation.chat.created", "Chat created", "Chat created"),
    CHAT_IN_PROGRESS("conversation.chat.in_progress", "Chat in progress", "Chat in progress"),
    CHAT_COMPLETED("conversation.chat.completed", "Chat completed", "Chat completed"),
    CHAT_FAILED("conversation.chat.failed", "Chat failed", "Chat failed"),
    CHAT_REQUIRES_ACTION("conversation.chat.requires_action", "Chat requires action", "Chat requires action"),
    MESSAGE_DELTA("conversation.message.delta", "Message delta", "Message delta"),
    MESSAGE_COMPLETED("conversation.message.completed", "Message completed", "Message completed"),
    ERROR("error", "Error", "Error"),
    DONE("done", "Done", "Done");

    private final String name;
    @JSONField
    private final String value;
    private final String description;

    StreamEventEnum(String value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    @JSONCreator
    public static StreamEventEnum create(String value) {
        Optional<StreamEventEnum> enumOptional = Arrays.stream(StreamEventEnum.values()).filter(e -> e.getValue().equals(value)).findFirst();
        if (enumOptional.isEmpty()) {
            throw new ConflictException("Unsupported enum value={}", value);
        }
        return enumOptional.get();
    }

    public boolean isChatEvent() {
        return this == CHAT_CREATED
                || this == CHAT_IN_PROGRESS
                || this == CHAT_COMPLETED
                || this == CHAT_FAILED
                || this == CHAT_REQUIRES_ACTION;
    }

    public boolean isMsgEvent() {
        return this == MESSAGE_DELTA
                || this == MESSAGE_COMPLETED;
    }

    public boolean isDone() {
        return this == DONE;
    }

    public boolean isError() {
        return this == ERROR;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
