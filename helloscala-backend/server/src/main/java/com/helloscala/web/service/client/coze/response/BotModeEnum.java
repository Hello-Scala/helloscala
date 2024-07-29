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
public enum BotModeEnum {
    SINGLE_AGENT(0, "Single agent", "Single agent mode"),
    MULTI_AGENT(1, "Multi agent", "Multiple agent mode");

    private final String name;
    @JSONField
    private final int value;
    private final String description;

    BotModeEnum(int value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    @JSONCreator
    public static BotModeEnum create(int value) {
        Optional<BotModeEnum> enumOptional = Arrays.stream(BotModeEnum.values()).filter(e -> e.getValue() == value).findFirst();
        if (enumOptional.isEmpty()) {
            throw new ConflictException("Unsupported enum value={}", value);
        }
        return enumOptional.get();
    }

    @Override
    public String toString() {
        return this.getValue() + "";
    }
}
