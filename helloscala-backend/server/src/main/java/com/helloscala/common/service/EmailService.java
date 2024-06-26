package com.helloscala.common.service;

import com.helloscala.common.entity.FriendLink;
import jakarta.mail.MessagingException;


public interface EmailService {
    void friendPassSendEmail(String email);

    void friendFailedSendEmail(String email,String reason);

    void emailNoticeMe(String subject,String content);

    void sendFriendEmail(FriendLink friendLink);

    void sendCode(String email) throws MessagingException;
}
