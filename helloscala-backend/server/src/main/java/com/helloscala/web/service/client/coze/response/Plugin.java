package com.helloscala.web.service.client.coze.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class Plugin {
    @JSONField(name = "plugin_id")
    private String pluginId;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "description")
    private String description;

    @JSONField(name = "icon_url")
    private String iconUrl;

    @JSONField(name = "api_info_list")
    private List<PluginAPIView> apiInfoList;
}