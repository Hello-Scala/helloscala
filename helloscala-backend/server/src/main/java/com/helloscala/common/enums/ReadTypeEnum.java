package com.helloscala.common.enums;

import lombok.Getter;

@Getter
public enum ReadTypeEnum {
    COMMENT(1),
    LIKE(2),
    CODE(3);

    public final int index;

    ReadTypeEnum(int index) {
        this.index = index;
    }
}
