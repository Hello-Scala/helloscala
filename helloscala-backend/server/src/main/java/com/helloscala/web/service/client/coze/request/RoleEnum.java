package com.helloscala.web.service.client.coze.request;

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
public enum RoleEnum {
    USER("user", "User", "Indicate message sent by user"),
    ASSISTANT("assistant", "Assistant", "Indicate message sent by bot");

    private final String name;
    @JSONField
    private final String value;
    private final String description;


    RoleEnum(String value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    @JSONCreator
    public static RoleEnum create(String value) {
        Optional<RoleEnum> enumOptional = Arrays.stream(RoleEnum.values()).filter(e -> e.getValue().equals(value)).findFirst();
        if (enumOptional.isEmpty()) {
            throw new ConflictException("Unsupported enum value={}", value);
        }
        return enumOptional.get();
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
