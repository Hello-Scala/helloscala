package com.helloscala.service.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.service.entity.AssistantMessage;
import com.helloscala.service.mapper.AssistantMessageMapper;
import com.helloscala.service.service.AssistantMessageService;
import org.springframework.stereotype.Service;


@Service
public class AssistantMessageServiceImpl extends ServiceImpl<AssistantMessageMapper, AssistantMessage> implements AssistantMessageService {
    @Override
    public Page<AssistantMessage> listByConversation(Page<AssistantMessage> page, String conversationId, String createTimeEnd) {
        if (StrUtil.isBlank(conversationId)) {
            return PageHelper.ofEmpty();
        }
        LambdaQueryWrapper<AssistantMessage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AssistantMessage::getConversationId, conversationId);
        queryWrapper.le(StrUtil.isNotBlank(createTimeEnd), AssistantMessage::getCreateTime, createTimeEnd);
        queryWrapper.orderByDesc(AssistantMessage::getCreateTime);
        return baseMapper.selectPage(page, queryWrapper);
    }
}
