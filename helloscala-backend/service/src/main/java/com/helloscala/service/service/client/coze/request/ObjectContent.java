package com.helloscala.web.service.client.coze.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class ObjectContent {
    @JSONField(name = "type")
    private ObjectContentTypeEnum type;

    @JSONField(name = "text")
    private String text;

    @JSONField(name = "file_id")
    private String fileId;

    @JSONField(name = "file_url")
    private String fileUrl;
}
