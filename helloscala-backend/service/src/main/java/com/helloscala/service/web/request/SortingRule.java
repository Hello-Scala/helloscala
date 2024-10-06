package com.helloscala.service.web.request;

import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class SortingRule {
    private String field;

    private Boolean desc = true;

    public SortingRule(String field) {
        this.field = field;
    }

    public SortingRule(String field, boolean desc) {
        this.field = field;
        this.desc = desc;
    }
}
