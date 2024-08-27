package com.helloscala.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helloscala.service.entity.AssistantConversation;
import com.helloscala.service.entity.AssistantMessage;
import org.springframework.stereotype.Repository;


@Repository
public interface AssistantMessageMapper extends BaseMapper<AssistantMessage> {

}
