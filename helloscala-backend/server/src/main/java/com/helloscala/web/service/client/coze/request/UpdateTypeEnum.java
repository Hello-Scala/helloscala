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
public enum UpdateTypeEnum {
    NOT_AUTO_UPDATE(0, "Not auto update", "Not auto update"),
    AUTO_UPDATE(1, "Auto update", "Auto update");

    private final String name;
    @JSONField
    private final int value;
    private final String description;

    UpdateTypeEnum(int value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    @JSONCreator
    public static UpdateTypeEnum create(int value) {
        Optional<UpdateTypeEnum> enumOptional = Arrays.stream(UpdateTypeEnum.values()).filter(e -> e.getValue() == value).findFirst();
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
