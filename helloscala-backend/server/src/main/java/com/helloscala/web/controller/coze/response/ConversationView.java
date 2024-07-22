package com.helloscala.web.controller.coze.response;

import lombok.Data;

import java.util.Date;

/**
 * @author Steve Zou
 */
@Data
public class ConversationView {
    private String id;

    private String botId;

    private String summary;

    private Date lastSendTime;

    private Date createTime;
}
