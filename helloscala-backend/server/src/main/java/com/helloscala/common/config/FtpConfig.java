package com.helloscala.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ftp")
public class FtpConfig {
    private String domain;

    private String host;

    private int port;

    private String basePath;
    private String storatePath;

    private String httpPath;

    private String username;

    private String password;
}
