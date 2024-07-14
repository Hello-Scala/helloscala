package com.helloscala.web.service.client.coze.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Map;

/**
 * @author Steve Zou
 */
@Data
public class CreateMsgRequest {
    @JSONField(name = "role")
    private RoleEnum role;

    @JSONField(name = "content")
    private String content;

    @JSONField(name = "content_type")
    private ContentTypeEnum contentType;

    @JSONField(name = "meta_data")
    private Map<String, String> metaData;
}
