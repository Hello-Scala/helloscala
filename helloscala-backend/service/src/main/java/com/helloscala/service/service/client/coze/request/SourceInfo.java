package com.helloscala.web.service.client.coze.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class SourceInfo {
    @JSONField(name = "file_base64")
    private String fileBase64;

    @JSONField(name = "file_type")
    private String fileType;

    @JSONField(name = "web_url")
    private String webUrl;

    @JSONField(name = "document_source")
    private Integer documentSource;
}
