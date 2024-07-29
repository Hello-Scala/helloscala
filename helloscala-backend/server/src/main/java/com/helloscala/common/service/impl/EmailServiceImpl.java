package com.helloscala.common.service.impl;

import com.helloscala.common.RedisConstants;
import com.helloscala.common.config.EmailConfig;
import com.helloscala.common.entity.FriendLink;
import com.helloscala.common.enums.FriendLinkEnum;
import com.helloscala.common.service.EmailService;
import com.helloscala.common.service.RedisService;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final RedisService redisService;
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final EmailConfig emailConfig;
    private final Map<Integer, Consumer<FriendLink>> map = new HashMap<>();

    @PostConstruct
    public void init() {
        map.put(FriendLinkEnum.UP.getCode(), friendLink -> friendPassSendEmail(friendLink.getEmail()));
        map.put(FriendLinkEnum.DOWN.getCode(), friendLink -> friendFailedSendEmail(friendLink.getEmail(), friendLink.getReason()));
    }


    @Async("threadPoolTaskExecutor")
    @Override
    public void emailNoticeMe(String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setFrom(emailConfig.getSendFrom());
        message.setTo(emailConfig.getNotify());
        message.setSentDate(new Date());
        message.setText(content);
        javaMailSender.send(message);
    }

    @Override
    public void sendFriendEmail(FriendLink friendLink) {
        Consumer<FriendLink> consumer = map.get(friendLink.getStatus());
        if (consumer != null) {
            consumer.accept(friendLink);
        }
    }

    @Override
    public void friendPassSendEmail(String email) {
        try {
            sendEmailByTemplate(email, emailConfig.getSubjectFriendPass(), emailConfig.getTemplateFriendPass(), Map.of());
            log.info("Send friend pass email success, email:{}", email);
        } catch (Exception e) {
            log.error("Failed to send email, email={}!", email, e);
        }
    }

    @Override
    public void friendFailedSendEmail(String email, String reason) {
        try {
            sendEmailByTemplate(email, emailConfig.getSubjectFriendFailed(), emailConfig.getTemplateFriendFailed(), Map.of("reason", reason));
            log.info("Send friend failed email success, email={}, reason={}", email, reason);
        } catch (Exception e) {
            log.error("Failed to send email, email={}, reason={}!", email, reason, e);
        }
    }

    @Override
    public void sendCode(String email) throws MessagingException {
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        Map<String, Object> variables = Map.of("code", code);
        sendEmailByTemplate(email, emailConfig.getSubjectCodeVerify(), emailConfig.getTemplateCodeVerify(), variables);
        log.info("Send email success, email:{}, code:{}", email, code);
        redisService.setCacheObject(RedisConstants.EMAIL_CODE + email, code + "");
        redisService.expire(RedisConstants.EMAIL_CODE + email, RedisConstants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
    }

    private void sendEmailByTemplate(String email, String subject, String template, Map<String, Object> variables) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setFrom(emailConfig.getSendFrom());

        Context context = new Context();
        context.setVariables(variables);
        String text = templateEngine.process(template, context);
        helper.setText(text, true);
        javaMailSender.send(message);
    }
}
