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
public enum DocumentTypeEnum {
    PDF("pdf", "pdf", "pdf"),
    TXT("txt", "txt", "txt"),
    DOC("doc", "doc", "doc"),
    DOCX("docx", "docx", "docx");

    private final String name;
    @JSONField
    private final String value;
    private final String description;

    DocumentTypeEnum(String value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    @JSONCreator
    public static DocumentTypeEnum create(String value) {
        Optional<DocumentTypeEnum> enumOptional = Arrays.stream(DocumentTypeEnum.values()).filter(e -> e.getValue().equals(value)).findFirst();
        if (enumOptional.isEmpty()) {
            throw new ConflictException("Unsupported enum value={}", value);
        }
        return enumOptional.get();

    }
}
