package com.helloscala.web.service;

import com.helloscala.common.ResponseResult;


public interface ApiHomeService {
    String report();

    ResponseResult getHomeData();

    ResponseResult getWebSiteInfo();

    String hot(String type);
}
