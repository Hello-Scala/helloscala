package com.helloscala.web.im;


import cn.hutool.json.JSONUtil;
import com.helloscala.common.utils.SpringUtil;
import com.helloscala.common.vo.message.ImMessageVO;
import com.helloscala.common.web.exception.BadRequestException;
import com.helloscala.web.service.ApiImMessageService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;




public class WebSocketSimpleChannelInboundHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketSimpleChannelInboundHandler.class);
    private final WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory(WebSocketConstant.WEB_SOCKET_URL, null, false);
    private WebSocketServerHandshaker handShaker;
    private final WebSocketInfoService websocketInfoService = new WebSocketInfoService();
    private void handWebsocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof CloseWebSocketFrame) {
            handShaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            websocketInfoService.clearSession(ctx.channel());
            return;
        }
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        if (frame instanceof PongWebSocketFrame) {
            ctx.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        if (!(frame instanceof TextWebSocketFrame)) {
            logger.error("binary msg unsupported!");
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            throw new BadRequestException("Message unsupported, " + this.getClass().getName());
        }
        String message = ((TextWebSocketFrame) frame).text();
        logger.info("Received client msg:{}", message);
        try {
            ImMessageVO imMessageVO = JSONUtil.toBean(message, ImMessageVO.class);
            switch (imMessageVO.getCode()) {
                case MessageConstant.GROUP_CHAT_CODE:
                    SessionHolder.channelGroup.writeAndFlush(new TextWebSocketFrame(message));
                    break;
                case MessageConstant.PRIVATE_CHAT_CODE:
                    String toUserId = imMessageVO.getToUserId();
                    String fromUserId = imMessageVO.getFromUserId();
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
                case MessageConstant.PONG_CHAT_CODE:
                    Channel channel = ctx.channel();
                    NettyAttrUtil.refreshLastHeartBeatTime(channel);
                default:
            }
        } catch(Exception e) {
            logger.error("Send msg failed:", e);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("--channelActive--");
//        ctx.channel().config().setWriteBufferHighWaterMark();
//        ctx.channel().config().setWriteBufferLowWaterMark();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("————客户端与服务端连接断开————");
        websocketInfoService.clearSession(ctx.channel());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Error:", cause);
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        if (o instanceof FullHttpRequest) {
            handHttpRequest(channelHandlerContext, (FullHttpRequest) o);
        } else if (o instanceof WebSocketFrame) {
            handWebsocketFrame(channelHandlerContext, (WebSocketFrame) o);
        }
    }

    private void handHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        if (!request.decoderResult().isSuccess()
                || !("websocket".equals(request.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        handShaker = factory.newHandshaker(request);
        if (handShaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            Map<String, String> params = RequestParamUtil.urlSplit(request.uri());
            String userId = params.get("userId");
            Channel channel = ctx.channel();
            NettyAttrUtil.setUserId(channel, userId);
            NettyAttrUtil.refreshLastHeartBeatTime(channel);
            handShaker.handshake(ctx.channel(), request);
            SessionHolder.channelGroup.add(ctx.channel());
            SessionHolder.channelMap.put(userId, ctx.channel());
            logger.info("Handshake success, uri：{}", request.uri());

//            Set<String> userList = SessionHolder.channelMap.keySet();
//            ApiImMessageService imMessageService = getImMessageService();
//            List<ImOnlineUserVO> onlineUserList = imMessageService.selectOnlineUserList(userList);
            ImMessageVO msg = new ImMessageVO();
            msg.setCode(MessageConstant.SYSTEM_MESSAGE_CODE);
            SessionHolder.channelGroup.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(msg)));

        }
    }

    @NotNull
    private static ApiImMessageService getImMessageService() {
        return SpringUtil.getBean(ApiImMessageService.class);
    }


    private void sendHttpResponse(ChannelHandlerContext ctx, DefaultFullHttpResponse response) {
        if (response.status().code() != 200) {
            ByteBuf byteBuf = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(byteBuf);
            byteBuf.release();
        }
        ChannelFuture channelFuture = ctx.channel().writeAndFlush(response);
        if (response.status().code() != 200) {
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }
}