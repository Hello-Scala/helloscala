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
public enum MsgContentTypeEnum {
    TEXT("text", "Text", "Text"),
    OBJECT_STRING("object_string", "Object string", "JSON string transferred from list of objects"),
    CARD("card", "Card", "Card");

    private final String name;
    @JSONField
    private final String value;
    private final String description;


    MsgContentTypeEnum(String value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    @JSONCreator
    public static MsgContentTypeEnum create(String value) {
        Optional<MsgContentTypeEnum> enumOptional = Arrays.stream(MsgContentTypeEnum.values()).filter(e -> e.getValue().equals(value)).findFirst();
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
