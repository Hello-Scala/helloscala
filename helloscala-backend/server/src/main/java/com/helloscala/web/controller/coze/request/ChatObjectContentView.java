package com.helloscala.web.controller.coze.request;

import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class ChatObjectContentView {
    private ContentTypeEnum type;

    private String text;

    private String fileId;

    private String fileUrl;
}
