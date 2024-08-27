package com.helloscala.service.service;


import com.helloscala.service.entity.Message;

import java.util.List;

public interface ApiMessageService {
    List<Message> list();

    void addMessage(Message message);
}
