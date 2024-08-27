package com.helloscala.web.service.client.coze.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class DocumentBase {
    @JSONField(name = "name")
    private String name;

    @JSONField(name = "source_info")
    private SourceInfo sourceInfo;

     @JSONField(name = "update_rule")
     private UpdateRule updateRule;

    // 省略 get 和 set 方法
}
