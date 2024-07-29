package com.helloscala.common.enums;

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
public enum SendFromEnum {
    ASSISTANT("ASSISTANT", "Assistant", "Assistant"),
    USER("USER", "User", "User");

    private final String name;
    @JSONField
    private final String value;
    private final String description;

    SendFromEnum(String value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    @JSONCreator
    public static SendFromEnum create(String value) {
        Optional<SendFromEnum> enumOptional = Arrays.stream(SendFromEnum.values()).filter(e -> e.getValue().equals(value)).findFirst();
        if (enumOptional.isEmpty()) {
            throw new ConflictException("Unsupported enum value={}", value);
        }
        return enumOptional.get();

    }
}
