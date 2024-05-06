package com.helloscala.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.FeedBack;
import com.helloscala.mapper.FeedBackMapper;
import com.helloscala.service.ApiFeedBackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApiFeedBackServiceImpl implements ApiFeedBackService {

    private final FeedBackMapper feedBackMapper;

    /**
     * 添加反馈
     *
     * @param feedBack
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addFeedback(FeedBack feedBack) {
        feedBack.setUserId(StpUtil.getLoginIdAsString());
        int rows = feedBackMapper.insert(feedBack);
        return rows > 0 ? ResponseResult.success() : ResponseResult.error("添加反馈失败");
    }
}
