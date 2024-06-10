package com.helloscala.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix = "qq")
public class QqConfigProperties {
    private String appId;

    private String appSecret;

    private String redirectUrl;
}
