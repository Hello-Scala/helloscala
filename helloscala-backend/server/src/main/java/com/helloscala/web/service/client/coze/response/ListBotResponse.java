package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author Steve Zou
 */
@Data
public class ListBotResponse {
    @JSONField(name = "space_bots")
    private List<BotSummaryView> spaceBots;

    @JSONField(name = "total")
    private int total;
}
