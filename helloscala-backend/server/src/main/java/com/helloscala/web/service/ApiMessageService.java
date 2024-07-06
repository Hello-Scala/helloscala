package com.helloscala.web.service;


import com.helloscala.common.entity.Message;

import java.util.List;

public interface ApiMessageService {
    List<Message> list();

    void addMessage(Message message);
}
