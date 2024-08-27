package com.helloscala.web.service.client.coze.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class UpdateKnowledgeFileRequest {
    @JSONField(name = "document_id")
    private String documentId;

    @JSONField(name = "document_name")
    private String documentName;

    @JSONField(name = "update_rule")
    private UpdateRule updateRule;
}