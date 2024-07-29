package com.helloscala.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.entity.AssistantConversation;
import com.helloscala.common.mapper.AssistantConversationMapper;
import com.helloscala.common.service.AssistantConversationService;
import com.helloscala.common.utils.SqlHelper;
import org.springframework.stereotype.Service;


@Service
public class AssistantConversationServiceImpl extends ServiceImpl<AssistantConversationMapper, AssistantConversation> implements AssistantConversationService {
    @Override
    public Page<AssistantConversation> list(String assistantId, Integer pageNo, Integer pageSize) {
        LambdaQueryWrapper<AssistantConversation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AssistantConversation::getBotId, assistantId);
        queryWrapper.orderByDesc(AssistantConversation::getCreateTime);
        Page<AssistantConversation> page = Page.of(pageNo, pageSize);
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public AssistantConversation getByCozeConversationId(String conversationId) {
        LambdaQueryWrapper<AssistantConversation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AssistantConversation::getConversationId, conversationId);
        queryWrapper.orderByDesc(AssistantConversation::getCreateTime);
        queryWrapper.last(SqlHelper.LIMIT_1);
        return baseMapper.selectOne(queryWrapper);
    }
}
