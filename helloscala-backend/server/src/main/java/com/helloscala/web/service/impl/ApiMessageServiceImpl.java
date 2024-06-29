package com.helloscala.web.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Message;
import com.helloscala.common.mapper.MessageMapper;
import com.helloscala.common.utils.IpUtil;
import com.helloscala.web.service.ApiMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiMessageServiceImpl implements ApiMessageService {
    private final MessageMapper messageMapper;

    @Override
    public ResponseResult list() {
        List<Message> messageList = messageMapper.selectList(new LambdaQueryWrapper<Message>()
                .select(Message::getId, Message::getNickname, Message::getAvatar,
                        Message::getContent, Message::getTime));
        return ResponseResult.success(messageList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addMessage(Message message) {
        String ipAddress = IpUtil.getIp();
        String ipSource = IpUtil.getIp2region(ipAddress);
        message.setIpAddress(ipAddress);
        message.setIpSource(ipSource);
        messageMapper.insert(message);
        return ResponseResult.success("Leave message success!");
    }
}
