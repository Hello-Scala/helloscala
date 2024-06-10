package com.helloscala.common.enums;


import lombok.Getter;

@Getter
public enum DataEventEnum {

    ES_ADD(0, "添加es数据"),

    ES_DELETE(1, "删除es数据"),

    ES_UPDATE(2, "修改es数据"),

    RESOURCE_ADD(3, "添加文件资源"),

    EMAIL_SEND(4, "发送邮件信息"),

    SYSTEM_NOTICE(5, "系统通知");

    DataEventEnum(int code, String desc) {
        this.type = code;
        this.desc = desc;
    }

    private final Integer type;
    private final String desc;
}
