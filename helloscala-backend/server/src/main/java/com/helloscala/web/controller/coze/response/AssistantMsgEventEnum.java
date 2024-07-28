package com.helloscala.web.controller.coze.response;

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
public enum AssistantMsgEventEnum {
    CHAT_CREATED("CHAT_CREATED", "Chat created", "Chat created"),
    CHAT_IN_PROGRESS("CHAT_IN_PROGRESS", "Chat in progress", "Chat in progress"),
    CHAT_COMPLETED("CHAT_COMPLETED", "Chat completed", "Chat completed"),
    CHAT_FAILED("CHAT_FAILED", "Chat failed", "Chat failed"),
    CHAT_REQUIRES_ACTION("CHAT_REQUIRES_ACTION", "Chat requires action", "Chat requires action"),
    MESSAGE_DELTA("MESSAGE_DELTA", "Message delta", "Message delta"),
    MESSAGE_COMPLETED("MESSAGE_COMPLETED", "Message completed", "Message completed"),
    ERROR("ERROR", "Error", "Error"),
    DONE("DONE", "Done", "Done");

    private final String name;
    @JSONField
    private final String value;
    private final String description;

    AssistantMsgEventEnum(String value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    @JSONCreator
    public static AssistantMsgEventEnum create(String value) {
        Optional<AssistantMsgEventEnum> enumOptional = Arrays.stream(AssistantMsgEventEnum.values()).filter(e -> e.getValue() == value).findFirst();
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
