package com.helloscala.web.service.client.coze.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class CreateKnowledgeFileRequest {
    @JSONField(name = "dataset_id")
    private String datasetId;

    @JSONField(name = "document_bases")
    private List<DocumentBase> documentBases;

    @JSONField(name = "chunk_strategy")
    private ChunkStrategy chunkStrategy;
}