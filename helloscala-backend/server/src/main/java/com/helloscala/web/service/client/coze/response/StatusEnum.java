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
public enum StatusEnum {
    PROCESSING(0, "Processing", "Processing"),
    SUCCESS(1, "Success", "Success"),
    FAILED(9, "Failed", "Failed");

    private final String name;
    @JSONField
    private final int value;
    private final String description;

    StatusEnum(int value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    @JSONCreator
    public static StatusEnum create(int value) {
        Optional<StatusEnum> enumOptional = Arrays.stream(StatusEnum.values()).filter(e -> e.getValue() == value).findFirst();
        if (enumOptional.isEmpty()) {
            throw new ConflictException("Unsupported enum value={}", value);
        }
        return enumOptional.get();

    }
}
