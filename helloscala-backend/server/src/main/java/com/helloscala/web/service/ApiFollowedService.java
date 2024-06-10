package com.helloscala.web.service;

import com.helloscala.common.ResponseResult;


public interface ApiFollowedService {
    ResponseResult addFollowedUser(String userId);

    ResponseResult deleteFollowed(String userId);
}
