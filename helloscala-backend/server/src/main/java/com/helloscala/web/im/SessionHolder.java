package com.helloscala.web.im;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionHolder {
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public static Map<String, Channel> channelMap = new ConcurrentHashMap<String, Channel>();
}


