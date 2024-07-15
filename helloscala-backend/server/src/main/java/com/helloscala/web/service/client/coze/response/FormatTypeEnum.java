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
public enum FormatTypeEnum {
    DOCUMENT(0, "Document", "Document"),
    SHEET(1, "Sheet", "Sheet"),
    IMAGE(2, "Image", "Image");

    private final String name;
    @JSONField
    private final int value;
    private final String description;

    FormatTypeEnum(int value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    @JSONCreator
    public static FormatTypeEnum create(int value) {
        Optional<FormatTypeEnum> enumOptional = Arrays.stream(FormatTypeEnum.values()).filter(e -> e.getValue() == value).findFirst();
        if (enumOptional.isEmpty()) {
            throw new ConflictException("Unsupported enum value={}", value);
        }
        return enumOptional.get();

    }
}
