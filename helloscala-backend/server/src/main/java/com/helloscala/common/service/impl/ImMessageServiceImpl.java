package com.helloscala.common.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.entity.ImMessage;
import com.helloscala.common.mapper.ImMessageMapper;
import com.helloscala.common.service.ImMessageService;
import org.springframework.stereotype.Service;



@Service
public class ImMessageServiceImpl extends ServiceImpl<ImMessageMapper, ImMessage> implements ImMessageService {
}
