package com.helloscala.web.service;

import com.helloscala.common.ResponseResult;


public interface ApiCollectService {
    ResponseResult selectCollectList();

    ResponseResult collect(Integer articleId);

    ResponseResult cancel(Integer articleId);
}
