package com.helloscala.common.enums;

/**
 * 文章发布状态枚举类
 */
public enum PublishEnum {
    SHELF(0),
    PUBLISH(1),
    AUDIO(2);

    public int code;

    PublishEnum(int code) {
        this.code = code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
