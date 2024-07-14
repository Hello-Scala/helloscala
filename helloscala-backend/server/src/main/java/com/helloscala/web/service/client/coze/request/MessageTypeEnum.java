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
public enum MessageTypeEnum {
    QUERY("query", "Query", "Message from user input"),
    ANSWER("answer", "Answer", "Message returned by bot"),
    FUNCTION_CALL("function_call", "Function call", "Result of function called by bot during chat"),
    TOOL_OUTPUT("tool_output", "Tool output", "Result of function call"),
    TOOL_RESPONSE("tool_response", "Tool response", "Result of function call"),
    FOLLOW_UP("follow_up", "Follow up", "Response of configured suggested question"),
    VERBOSE("verbose", "Verbose", "Message during multi answers scenario");

    private final String name;
    @JSONField
    private final String value;
    private final String description;


    MessageTypeEnum(String value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    @JSONCreator
    public static MessageTypeEnum create(String value) {
        Optional<MessageTypeEnum> enumOptional = Arrays.stream(MessageTypeEnum.values()).filter(e -> e.getValue().equals(value)).findFirst();
        if (enumOptional.isEmpty()) {
            throw new ConflictException("Unsupported enum value={}", value);
        }
        return enumOptional.get();
    }
}
