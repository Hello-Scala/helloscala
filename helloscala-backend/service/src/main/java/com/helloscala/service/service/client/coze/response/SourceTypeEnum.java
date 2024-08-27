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
public enum SourceTypeEnum {
    LOCAL_FILE(0, "Local file", "Local file"),
    WEB_PAGE(1, "Web page", "Web page");

    private final String name;
    @JSONField
    private final int value;
    private final String description;

    SourceTypeEnum(int value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    @JSONCreator
    public static SourceTypeEnum create(int value) {
        Optional<SourceTypeEnum> enumOptional = Arrays.stream(SourceTypeEnum.values()).filter(e -> e.getValue() == value).findFirst();
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
