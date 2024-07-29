package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.entity.AssistantConversation;
import com.helloscala.common.entity.AssistantMessage;


public interface AssistantMessageService extends IService<AssistantMessage> {
    Page<AssistantMessage> listByConversation(Page<AssistantMessage> page, String conversationId, String createTimeEnd);
}
