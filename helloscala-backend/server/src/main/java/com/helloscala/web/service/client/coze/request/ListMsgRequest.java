package com.helloscala.web.service.client.coze.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Map;

/**
 * @author Steve Zou
 */
@Data
public class ListMsgRequest {
    @JSONField(name = "chat_id")
    private String chatId;

    @JSONField(name = "before_id")
    private String beforeId;

    @JSONField(name = "after_id")
    private String afterId;

    @JSONField(name = "limit")
    private Integer limit;

    @JSONField(name = "order")
    private SortingOrderEnum order;
}
