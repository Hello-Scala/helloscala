package com.helloscala.web.service;

import com.helloscala.common.ResponseResult;
import com.helloscala.web.response.GetHomeInfoResponse;
import com.helloscala.web.response.GetWebSiteInfoResponse;


public interface ApiHomeService {
    String report();

    ResponseResult getHomeData();

    GetHomeInfoResponse getHomeDataV2();

    ResponseResult getWebSiteInfo();
    GetWebSiteInfoResponse getWebSiteInfoV2();

    String hot(String type);
}
