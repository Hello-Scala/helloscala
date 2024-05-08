package com.helloscala.service;

import com.helloscala.common.ResponseResult;
import com.helloscala.entity.FeedBack;

public interface ApiFeedBackService {
    /**
     * 添加反馈
     * @return
     */
    public ResponseResult addFeedback(FeedBack feedBack);


}
