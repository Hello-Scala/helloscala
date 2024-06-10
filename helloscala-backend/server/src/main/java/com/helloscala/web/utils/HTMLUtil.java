package com.helloscala.web.utils;


import java.util.Objects;

public class HTMLUtil {
    public static String deleteTag(String source) {
        String filteredSource = SensitiveUtil.filter(source);
        if (Objects.isNull(filteredSource) || filteredSource.isBlank()) {
            return "";
        }
        String adjustedSource = filteredSource.replaceAll("(?!<(img).*?>)<.*?>", "");
        return deleteHMTLTag(adjustedSource);
    }

    private static String deleteHMTLTag(String source) {
        if (Objects.isNull(source) || source.isBlank()) {
            return "";
        }
        source = source.replaceAll("&.{2,6}?;", "");
        source = source.replaceAll("<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>", "");
        source = source.replaceAll("<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>", "");
        return source;
    }
}
