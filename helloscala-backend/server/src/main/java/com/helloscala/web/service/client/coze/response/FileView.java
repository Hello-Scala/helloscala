package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class FileView {
    @JSONField(name = "id")
    private String id;

    @JSONField(name = "bytes")
    private Integer bytes;

    @JSONField(name = "created_at")
    private Long createdAt;

    @JSONField(name = "file_name")
    private String fileName;

}
