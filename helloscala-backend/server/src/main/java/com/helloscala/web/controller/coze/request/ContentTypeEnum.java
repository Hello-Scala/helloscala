package com.helloscala.web.controller.coze.request;

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
public enum ContentTypeEnum {
    TEXT("TEXT", "Text", "Text"),
    FILE("FILE", "File", "File"),
    IMAGE("IMAGE", "Image", "Image");

    private final String name;
    @JSONField
    private final String value;
    private final String description;


    ContentTypeEnum(String value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    @JSONCreator
    public static ContentTypeEnum create(String value) {
        Optional<ContentTypeEnum> enumOptional = Arrays.stream(ContentTypeEnum.values()).filter(e -> e.getValue().equals(value)).findFirst();
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
