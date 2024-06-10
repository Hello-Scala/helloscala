package com.helloscala.common.enums;

import lombok.Getter;

@Getter
public enum YesOrNoEnum {
    NO (0, "否"),

    YES (1, "是");

    YesOrNoEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private final Integer code;
    private final String message;

    public String getCodeToString() {
        return code + "";
    }
}
