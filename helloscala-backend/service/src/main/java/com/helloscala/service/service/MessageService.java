package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.Message;
import com.helloscala.service.web.view.MessageView;

import java.util.Set;


public interface MessageService extends IService<Message> {
    Page<MessageView> selectMessagePage(Page<?> page, String name);

    void deleteMessage(Set<String> ids);

    Integer countAll();
}
