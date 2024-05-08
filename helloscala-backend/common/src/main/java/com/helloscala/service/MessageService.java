package com.helloscala.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Message;

import java.util.List;



public interface MessageService extends IService<Message> {

    /**
     * 分页
     * @param name
     * @return
     */
    ResponseResult selectMessagePage(String name);

    /**
     * 删除
     * @param ids
     * @return
     */
    ResponseResult deleteMessage(List<Integer> ids);


}
