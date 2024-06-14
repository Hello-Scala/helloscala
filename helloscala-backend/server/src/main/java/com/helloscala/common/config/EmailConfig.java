package com.helloscala.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Steve Zou
 */
@Data
@Configuration
public class EmailConfig {
    @Value("${email.sendFrom}")
    private String sendFrom;

    @Value("${email.notify}")
    private String notify;

    @Value("${email.subject.codeVerify}")
    private String subjectCodeVerify;

    @Value("${email.subject.friendPass}")
    private String subjectFriendPass;

    @Value("${email.subject.friendFailed}")
    private String subjectFriendFailed;

    @Value("${email.template.codeVerify}")
    private String templateCodeVerify;

    @Value("${email.template.friendPass}")
    private String templateFriendPass;

    @Value("${email.template.friendFailed}")
    private String templateFriendFailed;
}
