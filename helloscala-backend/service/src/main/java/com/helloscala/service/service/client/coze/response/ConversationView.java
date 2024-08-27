package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Map;

/**
 * @author Steve Zou
 */
@Data
public class ConversationView {
    @JSONField(name = "id")
    private String id;

    @JSONField(name = "created_at")
    private long createdAt;

    @JSONField(name = "meta_data")
    private Map<String, String> metaData;
}
