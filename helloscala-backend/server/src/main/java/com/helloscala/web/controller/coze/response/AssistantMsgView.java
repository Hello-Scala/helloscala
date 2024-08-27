package com.helloscala.web.controller.coze.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.service.enums.ContentTypeEnum;
import com.helloscala.service.enums.SendFromEnum;
import com.helloscala.common.utils.DateUtil;
import lombok.Data;

import java.util.Date;

/**
 * @author Steve Zou
 */
@Data
public class AssistantMsgView {
    private String id;

    private String conversationId;

    private String botId;

    private SendFromEnum sendFrom;

    private AssistantMsgEventEnum event;

    private AssistantMsgTypeEnum type;

    private String content;

    private ContentTypeEnum contentType;

    private String userId;

    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;
}
