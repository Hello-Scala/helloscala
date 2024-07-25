package com.helloscala.web.websocket;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.helloscala.common.vo.message.ImMessageVO;
import com.helloscala.common.web.exception.ConflictException;
import com.helloscala.web.im.MessageConstant;
import jakarta.annotation.PostConstruct;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Steve Zou
 */
@Slf4j
@Component
@EnableWebSocket
@EnableScheduling
@ServerEndpoint("/websocket/{userId}")
public class ChatWebSocket {
    private static final ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        log.info("websocket registered!");
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId) {
        sessionPool.put(userId, session);
        log.info("session added, session pool size:" + sessionPool.size());
    }

    @OnClose
    public void onClose(@PathParam("userId") String userId) {
        try (Session session = sessionPool.remove(userId)) {
            session.close();
            log.debug("session closed, userId={}, session pool size={}", userId, sessionPool.size());
        } catch (Exception e) {
            throw new ConflictException("Failed to remove session, userId={}", userId);
        }

    }

    public void pushMessage(String userId, String message) {
        for (Map.Entry<String, Session> item : sessionPool.entrySet()) {
            if (item.getKey().contains(userId)) {
                Session session = item.getValue();
                try {
                    synchronized (session) {
                        log.debug("push message:" + message);
                        session.getBasicRemote().sendText(message);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public void pushMessage(String message) {
        try {
            for (Map.Entry<String, Session> item : sessionPool.entrySet()) {
                try {
                    item.getValue().getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            log.debug("【系统 WebSocket】群发消息:" + message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @OnMessage
    public void onMessage(String message, @PathParam(value = "userId") String userId) {
        // todo
    }

    @OnError
    public void onError(Session session, Throwable t) {
        log.error("session error!", t);
    }

    public void sendMessage(String userId, String message) {
    }

    @Scheduled(fixedDelay = 10000)
    public void sendPing() {
        ImMessageVO webSocketMessage = new ImMessageVO();
        webSocketMessage.setCode(MessageConstant.PING_MESSAGE_CODE);
        String message = JSONUtil.toJsonStr(webSocketMessage);
        sessionPool.forEach((userId, session) -> session.getAsyncRemote().sendText(message));
    }

    public void chat(ImMessageVO messageData) {
        String message = JSONUtil.toJsonStr(messageData);
        switch (messageData.getCode()) {
            case MessageConstant.PRIVATE_CHAT_CODE -> {
                String toUserId = messageData.getToUserId();
                String fromUserId = messageData.getFromUserId();
                sendMsg(sessionPool.get(toUserId), message);
                if (StrUtil.isNotBlank(fromUserId) && !toUserId.equals(fromUserId)) {
                    sendMsg(sessionPool.get(fromUserId), message);
                }
            }
            case MessageConstant.GROUP_CHAT_CODE, MessageConstant.SYSTEM_MESSAGE_CODE -> {
                sessionPool.forEach((userId, session) -> sendMsg(session, message));
            }
        }
    }

    private static void sendMsg(Session nullableSession, String message) {
        Optional.ofNullable(nullableSession).ifPresent(session -> session.getAsyncRemote().sendText(message));
    }
}
