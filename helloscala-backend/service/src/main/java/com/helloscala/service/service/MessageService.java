package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.Message;

import java.util.List;



public interface MessageService extends IService<Message> {
    Page<Message> selectMessagePage(String name);

    void deleteMessage(List<Integer> ids);

    Long countAll();
}
