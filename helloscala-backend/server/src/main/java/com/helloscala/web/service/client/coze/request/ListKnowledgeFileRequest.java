package com.helloscala.web.service.client.coze.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class ListKnowledgeFileRequest {
    @JSONField(name = "dataset_id")
    private String datasetId;

    @JSONField(name = "page_index")
    private Integer pageIndex;

    @JSONField(name = "page_size")
    private Integer pageSize;
}
