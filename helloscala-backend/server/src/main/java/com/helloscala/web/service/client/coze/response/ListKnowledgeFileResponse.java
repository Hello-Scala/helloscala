package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author Steve Zou
 */
@Data
public class ListKnowledgeFileResponse {
    @JSONField(name = "code")
    private String code;

    @JSONField(name = "msg")
    private int msg;

    @JSONField(name = "document_infos")
    List<DocumentInfoView> documentInfos;

    @JSONField(name = "total")
    private int total;
}
