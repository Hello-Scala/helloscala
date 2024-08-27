package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.helloscala.web.service.client.coze.request.ChunkStrategy;
import com.helloscala.web.service.client.coze.request.UpdateTypeEnum;
import lombok.Data;

@Data
public class DocumentInfoView {
    @JSONField(name = "char_count")
    private Integer charCount;

    @JSONField(name = "chunk_strategy")
    private ChunkStrategy chunkStrategy;

    @JSONField(name = "create_time")
    private Long createTime;

    @JSONField(name = "document_id")
    private String documentId;

    @JSONField(name = "format_type")
    private Integer formatType;

    @JSONField(name = "hit_count")
    private Integer hitCount;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "size")
    private Integer size;

    @JSONField(name = "slice_count")
    private Integer sliceCount;

    @JSONField(name = "source_type")
    private SourceTypeEnum sourceType;

    @JSONField(name = "status")
    private StatusEnum status;

    @JSONField(name = "type")
    private DocumentTypeEnum type;

    @JSONField(name = "update_interval")
    private Integer updateInterval;

    @JSONField(name = "update_time")
    private Long updateTime;

    @JSONField(name = "update_type")
    private UpdateTypeEnum updateType;
}
