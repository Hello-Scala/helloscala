package com.helloscala.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.service.entity.Message;
import com.helloscala.service.mapper.MessageMapper;
import com.helloscala.service.service.MessageService;
import com.helloscala.service.web.view.MessageView;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
    @Override
    public Page<MessageView> selectMessagePage(Page<?> page, String name) {
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), Message::getNickname, name)
                .orderByDesc(Message::getCreateTime);
        Page<Message> messagePage = baseMapper.selectPage(PageHelper.of(page), queryWrapper);
        return PageHelper.convertTo(messagePage, message -> {
            MessageView messageView = new MessageView();
            messageView.setId(message.getId());
            messageView.setContent(message.getContent());
            messageView.setNickname(message.getNickname());
            messageView.setAvatar(message.getAvatar());
            messageView.setIpAddress(message.getIpAddress());
            messageView.setTime(message.getTime());
            messageView.setIpSource(message.getIpSource());
            messageView.setStatus(message.getStatus());
            messageView.setCreateTime(message.getCreateTime());
            return messageView;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMessage(Set<String> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public Integer countAll() {
        return baseMapper.selectCount(null).intValue();
    }
}
