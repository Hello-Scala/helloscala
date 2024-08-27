package com.helloscala.service.enums;

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
public enum MsgTypeEnum {
    TEXT("TEXT", "Text", "Text"),
    IMAGE("IMAGE", "Image", "Image");

    private final String name;
    @JSONField
    private final String value;
    private final String description;


    MsgTypeEnum(String value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    @JSONCreator
    public static MsgTypeEnum create(String value) {
        Optional<MsgTypeEnum> enumOptional = Arrays.stream(MsgTypeEnum.values()).filter(e -> e.getValue().equals(value)).findFirst();
        if (enumOptional.isEmpty()) {
            throw new ConflictException("Unsupported enum value={}", value);
        }
        return enumOptional.get();
    }
}
