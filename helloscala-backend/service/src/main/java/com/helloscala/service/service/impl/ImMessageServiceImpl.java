package com.helloscala.service.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.service.entity.ImMessage;
import com.helloscala.service.mapper.ImMessageMapper;
import com.helloscala.service.service.ImMessageService;
import org.springframework.stereotype.Service;



@Service
public class ImMessageServiceImpl extends ServiceImpl<ImMessageMapper, ImMessage> implements ImMessageService {
}
