package com.helloscala.common.enums;


import lombok.Getter;

@Getter
public enum DataEventEnum {
    ES_ADD_ARTICLE(0, "Add article to es"),
    ES_DELETE_ARTICLE(1, "Delete article from es"),
    ES_UPDATE_ARTICLE(2, "Update article on es"),
    RESOURCE_ADD(3, "Add resource"),
    EMAIL_SEND(4, "Send email"),
    SYSTEM_NOTICE(5, "System notification");

    DataEventEnum(int code, String desc) {
        this.type = code;
        this.desc = desc;
    }

    private final Integer type;
    private final String desc;
}
