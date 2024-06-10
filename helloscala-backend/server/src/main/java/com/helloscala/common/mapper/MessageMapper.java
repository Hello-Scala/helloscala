package com.helloscala.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helloscala.common.entity.Message;
import org.springframework.stereotype.Repository;



@Repository
public interface MessageMapper extends BaseMapper<Message> {
}
