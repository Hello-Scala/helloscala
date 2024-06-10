package com.helloscala.common.enums;

public enum UserStatusEnum {
    DISABLED(0),
    NORMAL(1);

    public final int code;

    UserStatusEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
