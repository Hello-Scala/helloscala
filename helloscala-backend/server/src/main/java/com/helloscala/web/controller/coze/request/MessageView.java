package com.helloscala.web.controller.coze.request;

import com.helloscala.service.enums.MsgTypeEnum;
import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class MessageView {
    private MsgTypeEnum msgType;
    private String content;
}
