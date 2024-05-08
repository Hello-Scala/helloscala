package com.helloscala.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Message;
import com.helloscala.mapper.MessageMapper;
import com.helloscala.service.ApiMessageService;
import com.helloscala.utils.IpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiMessageServiceImpl implements ApiMessageService {

    private final MessageMapper messageMapper;


    /**
     * 留言列表
     * @return
     */
    @Override
    public ResponseResult selectMessageList() {
        // 查询留言列表
        List<Message> messageList = messageMapper.selectList(new LambdaQueryWrapper<Message>()
                .select(Message::getId, Message::getNickname, Message::getAvatar,
                        Message::getContent, Message::getTime));
        return ResponseResult.success(messageList);
    }

    /**
     * 添加留言
     * @param message
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addMessage(Message message) {
        // 获取用户ip
        String ipAddress = IpUtil.getIp();
        String ipSource = IpUtil.getIp2region(ipAddress);
        message.setIpAddress(ipAddress);
        message.setIpSource(ipSource);
        messageMapper.insert(message);
        return ResponseResult.success("留言成功");
    }
}
