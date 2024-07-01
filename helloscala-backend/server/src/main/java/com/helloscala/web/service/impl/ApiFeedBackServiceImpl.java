package com.helloscala.web.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.helloscala.common.entity.FeedBack;
import com.helloscala.common.mapper.FeedBackMapper;
import com.helloscala.common.web.exception.ConflictException;
import com.helloscala.web.service.ApiFeedBackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApiFeedBackServiceImpl implements ApiFeedBackService {

    private final FeedBackMapper feedBackMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFeedback(FeedBack feedBack) {
        feedBack.setUserId(StpUtil.getLoginIdAsString());
        int rows = feedBackMapper.insert(feedBack);
        if (rows <= 0) {
            throw new ConflictException("Failed to add feedback!");
        }
    }
}
