package com.helloscala.service.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.service.enums.ContentTypeEnum;
import com.helloscala.service.enums.SendFromEnum;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author Steve Zou
 */
@Data
@TableName("b_assistant_message")
@Schema(name = "AI助手会话消息", description = "AI助手会话消息")
public class AssistantMessage {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String conversationId;

    private String botId;

    private String messageId;

    private SendFromEnum sendFrom;

    private String content;

    private ContentTypeEnum contentType;

    private String userId;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;

    private String createBy;
}
