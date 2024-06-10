package com.helloscala.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "gitee")
public class GiteeConfigProperties {
    private String appId;

    private String appSecret;

    private String redirectUrl;
}

