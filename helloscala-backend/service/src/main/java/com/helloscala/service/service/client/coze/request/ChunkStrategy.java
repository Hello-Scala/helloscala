package com.helloscala.web.service.client.coze.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ChunkStrategy {
    @JSONField(name = "chunk_type")
    private ChunkTypeEnum chunkType;

    @JSONField(name = "separator")
    private String separator;

    @JSONField(name = "max_tokens")
    private Integer maxTokens;

    @JSONField(name = "remove_extra_spaces")
    private Boolean removeExtraSpaces;

    @JSONField(name = "remove_urls_emails")
    private Boolean removeUrlsEmails;
}
