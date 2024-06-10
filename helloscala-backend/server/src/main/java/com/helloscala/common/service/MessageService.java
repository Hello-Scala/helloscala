package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Message;

import java.util.List;



public interface MessageService extends IService<Message> {
    ResponseResult selectMessagePage(String name);

    ResponseResult deleteMessage(List<Integer> ids);


}
