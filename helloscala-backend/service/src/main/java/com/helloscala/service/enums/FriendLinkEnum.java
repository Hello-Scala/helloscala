package com.helloscala.service.enums;

import lombok.Getter;

@Getter
public enum FriendLinkEnum {
    DOWN(0, "下架"),

    APPLY(1, "申请"),

    UP(2, "上架");


    FriendLinkEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public final Integer code;
    public final String message;

}
