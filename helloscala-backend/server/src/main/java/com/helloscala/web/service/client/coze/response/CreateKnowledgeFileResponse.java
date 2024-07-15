package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author Steve Zou
 */
@Data
public class CreateKnowledgeFileResponse {
    @JSONField(name = "code")
    private Integer code;

    @JSONField(name = "msg")
    private String msg;

    @JSONField(name = "document_infos")
    private List<DocumentInfoView> documentInfos;
}
