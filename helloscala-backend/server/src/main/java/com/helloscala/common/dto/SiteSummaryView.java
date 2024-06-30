package com.helloscala.common.dto;

import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class SiteSummaryView {
    private Long articleCount;

    private Long messageCount;

    private Long userCount;

    private Long viewsCount;
}
