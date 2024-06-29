package com.helloscala.web.service;


import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Message;

public interface ApiMessageService {
    ResponseResult list();

    ResponseResult addMessage(Message message);
}
