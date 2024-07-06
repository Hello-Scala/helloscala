package com.helloscala.web.service;

import com.helloscala.web.response.GetHomeInfoResponse;
import com.helloscala.web.response.GetWebSiteInfoResponse;


public interface ApiHomeService {
    String report();

    GetHomeInfoResponse getHomeDataV2();

    GetWebSiteInfoResponse getWebSiteInfoV2();

    String hot(String type);
}
