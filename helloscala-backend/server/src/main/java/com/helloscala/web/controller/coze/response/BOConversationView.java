package com.helloscala.web.controller.coze.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import lombok.Data;

import java.util.Date;

/**
 * @author Steve Zou
 */
@Data
public class BOConversationView {
    private String id;

    private String botId;

    private String summary;

    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date lastSendTime;

    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;
}
