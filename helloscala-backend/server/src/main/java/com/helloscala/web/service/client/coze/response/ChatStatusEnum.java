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
public enum ChatStatusEnum {
    CREATED("created", "Created", "Chat created"),
    IN_PROGRESS("in_progress", "In progress", "Bot are processing"),
    COMPLETED("completed", "Completed", "Bot process complete, chat end"),
    FAILED("failed", "Failed", "Failed"),
    REQUIRES_ACTION("requires_action", "Requires action", "Chat breakdown, requires action");


    private final String name;
    @JSONField
    private final String value;
    private final String description;

    ChatStatusEnum(String value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    @JSONCreator
    public static ChatStatusEnum create(String value) {
        Optional<ChatStatusEnum> enumOptional = Arrays.stream(ChatStatusEnum.values()).filter(e -> e.getValue().equals(value)).findFirst();
        if (enumOptional.isEmpty()) {
            throw new ConflictException("Unsupported enum value={}", value);
        }
        return enumOptional.get();

    }
}
