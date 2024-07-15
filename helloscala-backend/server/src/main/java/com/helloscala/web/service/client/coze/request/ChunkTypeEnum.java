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
public enum ChunkTypeEnum {
    AUTO_SPLIT_CLEAN(0, "Auto split an clean", "Auto split an clean"),
    MANUAL(1, "Manual", "Manual");

    private final String name;
    @JSONField
    private final int value;
    private final String description;

    ChunkTypeEnum(int value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    @JSONCreator
    public static ChunkTypeEnum create(int value) {
        Optional<ChunkTypeEnum> enumOptional = Arrays.stream(ChunkTypeEnum.values()).filter(e -> e.getValue() == value).findFirst();
        if (enumOptional.isEmpty()) {
            throw new ConflictException("Unsupported enum value={}", value);
        }
        return enumOptional.get();

    }
}
