package com.helloscala.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.entity.AssistantConversation;
import com.helloscala.common.mapper.AssistantConversationMapper;
import com.helloscala.common.service.AssistantConversationService;
import org.springframework.stereotype.Service;


@Service
public class AssistantConversationServiceImpl extends ServiceImpl<AssistantConversationMapper, AssistantConversation> implements AssistantConversationService {
    @Override
    public IPage<AssistantConversation> list(String assistantId, Integer pageNo, Integer pageSize) {
        LambdaQueryWrapper<AssistantConversation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AssistantConversation::getBotId, assistantId);
        queryWrapper.orderByDesc(AssistantConversation::getCreateTime);
        IPage<AssistantConversation> page = PageDTO.of(pageNo, pageSize);
        return baseMapper.selectPage(page, queryWrapper);
    }
}
