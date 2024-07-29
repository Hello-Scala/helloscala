package com.helloscala.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author Steve Zou
 */
@Data
@TableName("b_assistant_conversation")
@Schema(name = "AI助手会话", description = "AI助手会话")
public class AssistantConversation {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String conversationId;

    private String botId;

    private String userId;

    private String summary;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date lastSendTime;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;

    private String createBy;
}
