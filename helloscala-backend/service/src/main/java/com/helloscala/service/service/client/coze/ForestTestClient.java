package com.helloscala.web.service.client.coze;

import com.dtflys.forest.annotation.Get;

/**
 * @author Steve Zou
 */
public interface ForestTestClient {
    @Get("http://localhost:8400/helloscala")
    String test();
}
