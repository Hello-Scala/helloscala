package com.helloscala.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginTypeEnum {

    EMAIL(1, "email"),
    QQ(2, "qq"),
    WEIBO(3, "weibo"),
    GITEE(4, "gitee"),
    WECHAT(5, "微信登录"),
    GITHUB(6, "github");

    private final Integer type;
    private final String desc;
    public static Integer getType(String desc) {
        for (LoginTypeEnum value : LoginTypeEnum.values()) {
            if (value.getDesc().equals(desc)) {
                return value.getType();
            }
        }
        return null;
    }
}
