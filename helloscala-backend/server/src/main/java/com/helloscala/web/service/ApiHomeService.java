package com.helloscala.web.service;

import com.helloscala.common.ResponseResult;


public interface ApiHomeService {
    ResponseResult report();

    ResponseResult getHomeData();

    ResponseResult getWebSiteInfo();

    ResponseResult hot(String type);
}
