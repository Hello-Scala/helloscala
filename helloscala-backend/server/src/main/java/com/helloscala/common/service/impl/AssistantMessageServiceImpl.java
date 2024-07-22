package com.helloscala.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.entity.AssistantMessage;
import com.helloscala.common.mapper.AssistantMessageMapper;
import com.helloscala.common.service.AssistantMessageService;
import org.springframework.stereotype.Service;


@Service
public class AssistantMessageServiceImpl extends ServiceImpl<AssistantMessageMapper, AssistantMessage> implements AssistantMessageService {

}
