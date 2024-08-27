package com.helloscala.web.service.client.coze.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class DeleteKnowledgeFileRequest {
    @JSONField(name = "document_ids")
    private List<String> documentIds;
}