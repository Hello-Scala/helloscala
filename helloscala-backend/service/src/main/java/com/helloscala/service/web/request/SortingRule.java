package com.helloscala.service.web.request;

import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class SortingRule {
    private String field;

    private Boolean desc = true;
}
