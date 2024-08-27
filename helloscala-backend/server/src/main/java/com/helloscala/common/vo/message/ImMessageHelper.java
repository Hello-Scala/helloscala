package com.helloscala.common.vo.message;

import com.helloscala.service.enums.ContentTypeEnum;
import com.helloscala.service.enums.MsgTypeEnum;
import com.helloscala.service.service.client.coze.request.MsgContentTypeEnum;

/**
 * @author Steve Zou
 */
public final class ImMessageHelper {
    public static int toType(ContentTypeEnum contentType) {
        return switch (contentType) {
            case TEXT -> 1;
            case OBJECT_STRING -> 3;
            case CARD -> 4;
        };
    }

    public static int toType(MsgContentTypeEnum contentType) {
        return switch (contentType) {
            case TEXT -> 1;
            case OBJECT_STRING -> 3;
            case CARD -> 4;
        };
    }

    public static int toType(MsgTypeEnum msgType) {
        return switch (msgType) {
            case TEXT -> 1;
            case IMAGE -> 2;
        };
    }
}
