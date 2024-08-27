package com.helloscala.admin.service;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Steve Zou
 */
@Data
public class BORedisCacheStateView {
    private Map<Object, Object> info;

    private List<Map<String, String>> commandStats;

    private Long dbSize;
}
