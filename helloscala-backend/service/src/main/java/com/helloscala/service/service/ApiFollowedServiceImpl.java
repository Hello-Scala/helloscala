package com.helloscala.service.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.helloscala.service.entity.Followed;
import com.helloscala.service.mapper.FollowedMapper;
import com.helloscala.common.web.exception.BadRequestException;
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
    public void addFollowedUser(String userId) {
        if (StringUtils.isBlank(userId)) {
            throw new BadRequestException("Followed user id is empty!");
        }
        if (userId.equals(StpUtil.getLoginIdAsString())) {
            throw new BadRequestException("Can not follow yourself!");
        }
        Followed followed = Followed.builder().userId(StpUtil.getLoginIdAsString()).followedUserId(userId).build();
        followedMapper.insert(followed);
        SystemNoticeHandle.sendNotice(userId, MessageConstant.MESSAGE_WATCH_NOTICE, MessageConstant.SYSTEM_MESSAGE_CODE, null, null, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFollowed(String userId) {
        if (StringUtils.isBlank(userId)) {
            throw new BadRequestException("Followed user id is empty!");
        }
        LambdaQueryWrapper<Followed> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Followed::getUserId, StpUtil.getLoginIdAsString())
                .eq(Followed::getFollowedUserId, userId);
        followedMapper.delete(queryWrapper);
    }
}
