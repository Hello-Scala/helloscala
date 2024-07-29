package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.entity.AssistantConversation;


public interface AssistantConversationService extends IService<AssistantConversation> {
    Page<AssistantConversation> list(String assistantId, Integer pageNo, Integer pageSize);

    AssistantConversation getByCozeConversationId(String conversationId);
}
