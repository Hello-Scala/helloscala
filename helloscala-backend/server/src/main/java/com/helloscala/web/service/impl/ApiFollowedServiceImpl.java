package com.helloscala.web.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Followed;
import com.helloscala.common.exception.BusinessException;
import com.helloscala.common.mapper.FollowedMapper;
import com.helloscala.web.handle.SystemNoticeHandle;
import com.helloscala.web.im.MessageConstant;
import com.helloscala.web.service.ApiFollowedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ApiFollowedServiceImpl implements ApiFollowedService {

    private final FollowedMapper followedMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addFollowedUser(String userId) {
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException("Followed user id is empty!");
        }
        if (userId.equals(StpUtil.getLoginIdAsString())) {
            throw new BusinessException("Can not follow yourself!");
        }
        Followed followed = Followed.builder().userId(StpUtil.getLoginIdAsString()).followedUserId(userId).build();
        followedMapper.insert(followed);
        SystemNoticeHandle.sendNotice(userId, MessageConstant.MESSAGE_WATCH_NOTICE, MessageConstant.SYSTEM_MESSAGE_CODE,null,null,null);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteFollowed(String userId) {
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException("Followed user id is empty!");
        }
        followedMapper.delete(new LambdaQueryWrapper<Followed>().eq(Followed::getUserId,StpUtil.getLoginIdAsString()).eq(Followed::getFollowedUserId,userId));
        return ResponseResult.success();
    }
}
