package com.helloscala.web.controller.home;

import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class APIGetWebSiteInfoResponse {
    private APIWebConfigView config;

    private Long blogViewCount;

    private Long visitorCount;
}
