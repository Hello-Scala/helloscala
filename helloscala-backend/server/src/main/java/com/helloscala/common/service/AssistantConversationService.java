package com.helloscala.common.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.entity.AssistantConversation;


public interface AssistantConversationService extends IService<AssistantConversation> {
    IPage<AssistantConversation> list(String assistantId, Integer pageNo, Integer pageSize);
}
