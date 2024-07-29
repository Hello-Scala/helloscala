package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class BotSummaryView {
    @JSONField(name = "bot_id")
    private String botId;

    @JSONField(name = "bot_name")
    private String botName;

    @JSONField(name = "description")
    private String description;

    @JSONField(name = "icon_url")
    private String iconUrl;

    @JSONField(name = "publish_time")
    private long publishTime;
}
