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
public enum ObjectContentTypeEnum {
    TEXT("text", "Text", "Text"),
    FILE("file", "File", "File"),
    IMAGE("image", "Image", "Image");

    private final String name;
    @JSONField
    private final String value;
    private final String description;


    ObjectContentTypeEnum(String value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    @JSONCreator
    public static ObjectContentTypeEnum create(String value) {
        Optional<ObjectContentTypeEnum> enumOptional = Arrays.stream(ObjectContentTypeEnum.values()).filter(e -> e.getValue().equals(value)).findFirst();
        if (enumOptional.isEmpty()) {
            throw new ConflictException("Unsupported enum value={}", value);
        }
        return enumOptional.get();
    }
}
