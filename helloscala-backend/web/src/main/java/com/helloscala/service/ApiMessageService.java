package com.helloscala.service;


import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Message;

public interface ApiMessageService {

    /**
     * 获取所有留言
     * @return
     */
    ResponseResult selectMessageList();

    /**
     * 添加留言
     * @param message
     * @return
     */
    ResponseResult addMessage(Message message);
}
