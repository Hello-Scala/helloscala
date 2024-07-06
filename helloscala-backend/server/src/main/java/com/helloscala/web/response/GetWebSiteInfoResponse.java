package com.helloscala.web.response;

import com.helloscala.common.entity.WebConfig;
import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class GetWebSiteInfoResponse {
    private WebConfig config;

    private Long blogViewCount;

    private Long visitorCount;
}
