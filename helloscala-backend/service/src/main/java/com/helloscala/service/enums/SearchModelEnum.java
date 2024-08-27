package com.helloscala.service.enums;

import lombok.Getter;

@Getter
public enum SearchModelEnum {

    MYSQL(0, "mysql搜索", "mysqlSearch"),

    ELASTICSEARCH(1, "elasticsearch搜索", "elasticsearch");

    SearchModelEnum(int code, String desc, String strategy) {
        this.type = code;
        this.desc = desc;
        this.strategy = strategy;
    }


    private final Integer type;
    private final String desc;
    private final String strategy;

    public static String getStrategy(int type) {
        for (SearchModelEnum value : SearchModelEnum.values()) {
            if (value.getType() == (type)) {
                return value.getStrategy();
            }
        }
        return null;
    }
}
