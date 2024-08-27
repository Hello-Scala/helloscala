package com.helloscala.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.service.entity.Message;
import com.helloscala.service.mapper.MessageMapper;
import com.helloscala.service.service.MessageService;
import com.helloscala.common.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
    @Override
    public Page<Message> selectMessagePage(String name) {
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name),Message::getNickname,name)
                .orderByDesc(Message::getCreateTime);
        Page<Message> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        return baseMapper.selectPage(page,queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMessage(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
    }

}
