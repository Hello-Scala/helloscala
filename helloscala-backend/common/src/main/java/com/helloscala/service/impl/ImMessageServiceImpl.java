package com.helloscala.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.entity.ImMessage;
import com.helloscala.mapper.ImMessageMapper;
import com.helloscala.service.ImMessageService;
import org.springframework.stereotype.Service;



@Service
public class ImMessageServiceImpl extends ServiceImpl<ImMessageMapper, ImMessage> implements ImMessageService {


}
