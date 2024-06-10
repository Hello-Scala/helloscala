package com.helloscala.web.im;


import cn.hutool.json.JSONUtil;
import com.helloscala.common.vo.message.ImMessageVO;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebSocketInfoService {
    public void clearSession(Channel channel) {
        String userId = NettyAttrUtil.getUserId(channel);
        // 清除会话信息
        SessionHolder.channelGroup.remove(channel);
        SessionHolder.channelMap.remove(userId);
    }

    public void sendPing() {
        ImMessageVO webSocketMessage = new ImMessageVO();
        webSocketMessage.setCode(MessageConstant.PING_MESSAGE_CODE);
        String message = JSONUtil.toJsonStr(webSocketMessage);
        TextWebSocketFrame tws = new TextWebSocketFrame(message);
        SessionHolder.channelGroup.writeAndFlush(tws);
    }

    public void scanNotActiveChannel() {
        Map<String, Channel> channelMap = SessionHolder.channelMap;
        if (channelMap.isEmpty()) {
            return;
        }
        for (Channel channel : channelMap.values()) {
            if (!channel.isOpen()
                || !channel.isActive()) {
                String userId = NettyAttrUtil.getUserId(channel);
                channelMap.remove(userId);
                SessionHolder.channelGroup.remove(channel);
                if (channel.isOpen() || channel.isActive()) {
                    channel.close();
                }
            }
        }
    }

    public void chat(ImMessageVO messageData) {
        String message = JSONUtil.toJsonStr(messageData);
        switch (messageData.getCode()) {
            case MessageConstant.GROUP_CHAT_CODE:
                SessionHolder.channelGroup.writeAndFlush(new TextWebSocketFrame(message));
                break;
            case MessageConstant.PRIVATE_CHAT_CODE:
                String toUserId = messageData.getToUserId();
                String fromUserId = messageData.getFromUserId();
                for (Map.Entry<String, Channel> entry : SessionHolder.channelMap.entrySet()) {
                    String userId = entry.getKey();
                    Channel channel = entry.getValue();
                    if (toUserId.equals(userId)) {
                        channel.writeAndFlush(new TextWebSocketFrame(message));
                    }
                }
                if (!toUserId.equals(fromUserId)) {
                    SessionHolder.channelMap.get(fromUserId).writeAndFlush(new TextWebSocketFrame(message));
                }
                break;
            case MessageConstant.SYSTEM_MESSAGE_CODE:
                SessionHolder.channelGroup.writeAndFlush(new TextWebSocketFrame(message));
                break;
            default:
        }
    }
}
