package com.helloscala.web.im;


import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;


public class NettyAttrUtil {

    private static final AttributeKey<String> ATTR_KEY_LAST_HEARTBEAT_TIME = AttributeKey.valueOf("lastHeartBeatTime");

    private static final AttributeKey<String> ATTR_KEY_USER_ID = AttributeKey.valueOf("userId");

    public static void refreshLastHeartBeatTime(Channel channel) {
        Long now = System.currentTimeMillis();
        channel.attr(ATTR_KEY_LAST_HEARTBEAT_TIME).set(now.toString());
    }

    public static Long getLastHeartBeatTime(Channel channel) {
        String value = getAttribute(channel, ATTR_KEY_LAST_HEARTBEAT_TIME);
        if (value != null) {
            return Long.valueOf(value);
        }
        return null;
    }

    public static void setUserId(Channel channel, String value) {
        channel.attr(ATTR_KEY_USER_ID).set(value);
    }

    public static String getUserId(Channel channel) {
        return getAttribute(channel, ATTR_KEY_USER_ID);
    }

    private static String getAttribute(Channel channel, AttributeKey<String> key) {
        Attribute<String> attr = channel.attr(key);
        return attr.get();
    }
}


