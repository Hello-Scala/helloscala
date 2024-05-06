package com.helloscala.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helloscala.entity.Message;
import org.springframework.stereotype.Repository;



@Repository
public interface MessageMapper extends BaseMapper<Message> {

}
