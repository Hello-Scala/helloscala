package com.helloscala.web.service;

import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.FeedBack;

public interface ApiFeedBackService {
    ResponseResult addFeedback(FeedBack feedBack);
}
