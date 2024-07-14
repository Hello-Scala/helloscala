package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author Steve Zou
 */
@Data
public class BotDetailView {
    @JSONField(name = "bot_id")
    private String botId;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "description")
    private String description;

    @JSONField(name = "icon_url")
    private String iconUrl;

    @JSONField(name = "create_time")
    private long createTime;

    @JSONField(name = "update_time")
    private long updateTime;

    @JSONField(name = "version")
    private String version;

    @JSONField(name = "prompt_info")
    private PromptInfo promptInfo;

    @JSONField(name = "onboarding_info")
    private OnboardingInfo onboardingInfo;

    @JSONField(name = "bot_mode")
    private int botMode;

    @JSONField(name = "model_info")
    private ModelView modelInfo;

    @JSONField(name = "plugin_info_list")
    private List<Plugin> pluginInfoList; // 使用 Object 作为占位符，因为具体类型未知
}
