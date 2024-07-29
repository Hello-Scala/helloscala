package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author Steve Zou
 */
@Data
public class ListMsgResponse {
    @JSONField(name = "code")
    private String code;

    @JSONField(name = "msg")
    private int msg;

    @JSONField(name = "data")
    private List<MsgView> data;

    @JSONField(name = "first_id")
    private String firstId;

    @JSONField(name = "last_id")
    private String lastId;

    @JSONField(name = "has_more")
    private boolean hasMore;
}
