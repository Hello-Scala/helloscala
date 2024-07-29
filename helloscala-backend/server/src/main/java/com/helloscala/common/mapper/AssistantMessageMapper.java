package com.helloscala.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helloscala.common.entity.AssistantConversation;
import com.helloscala.common.entity.AssistantMessage;
import org.springframework.stereotype.Repository;


@Repository
public interface AssistantMessageMapper extends BaseMapper<AssistantMessage> {

}
