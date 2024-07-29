package com.helloscala.web.config;

import com.alibaba.fastjson.annotation.JSONField;
import com.dtflys.forest.config.ForestConfiguration;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "coze")
public class CozeConfiguration {
    private String baseUrl;

    private String accessToken;

    private String spaceId;

    private String botId;

    @PostConstruct
    public void init() {
        ForestConfiguration forestConfiguration = ForestConfiguration.getDefaultConfiguration();
        forestConfiguration.setVariableValue("cozeBaseUrl", baseUrl);
        forestConfiguration.setVariableValue("cozeAccessToken", accessToken);
        forestConfiguration.setVariableValue("cozeSpaceId", spaceId);
        forestConfiguration.setVariableValue("cozeBotId", botId);
    }
}
