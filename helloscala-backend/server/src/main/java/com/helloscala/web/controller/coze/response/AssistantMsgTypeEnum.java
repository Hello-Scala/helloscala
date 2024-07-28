package com.helloscala.web.controller.coze.response;

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
public enum AssistantMsgTypeEnum {
    QUERY("QUERY", "Query", "Message from user input"),
    ANSWER("ANSWER", "Answer", "Message returned by bot"),
    FUNCTION_CALL("FUNCTION_CALL", "Function call", "Result of function called by bot during chat"),
    TOOL_OUTPUT("TOOL_OUTPUT", "Tool output", "Result of function call"),
    TOOL_RESPONSE("TOOL_RESPONSE", "Tool response", "Result of function call"),
    FOLLOW_UP("FOLLOW_UP", "Follow up", "Response of configured suggested question"),
    VERBOSE("VERBOSE", "Verbose", "Message during multi answers scenario");

    private final String name;
    @JSONField
    private final String value;
    private final String description;


    AssistantMsgTypeEnum(String value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    @JSONCreator
    public static AssistantMsgTypeEnum create(String value) {
        Optional<AssistantMsgTypeEnum> enumOptional = Arrays.stream(AssistantMsgTypeEnum.values()).filter(e -> e.getValue().equals(value)).findFirst();
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
