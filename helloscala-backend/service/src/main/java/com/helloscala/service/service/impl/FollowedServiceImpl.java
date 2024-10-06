package com.helloscala.service.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.web.exception.BadRequestException;
import com.helloscala.service.entity.Collect;
import com.helloscala.service.entity.Followed;
import com.helloscala.service.mapper.CollectMapper;
import com.helloscala.service.mapper.FollowedMapper;
import com.helloscala.service.service.CollectService;
import com.helloscala.service.service.FollowedService;
import com.helloscala.service.web.view.CollectCountView;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Steve Zou
 */
@Service
@RequiredArgsConstructor
public class FollowedServiceImpl extends ServiceImpl<FollowedMapper, Followed> implements FollowedService {
    private final SystemNoticeHandle systemNoticeHandle;

    @Override
    public List<String> listFollowedUserIds(String userId) {
        if (StrUtil.isBlank(userId)) {
            return List.of();
        }
        LambdaQueryWrapper<Followed> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Followed::getId, Followed::getUserId, Followed::getFollowedUserId).eq(Followed::getUserId, userId);
        List<Followed> collects = baseMapper.selectList(queryWrapper);
        return collects.stream().map(Followed::getFollowedUserId).toList();
    }

    @Override
    public void addFollowed(String userId, String followingUserId) {
        if (StringUtils.isBlank(userId)) {
            throw new BadRequestException("Followed user id is empty!");
        }
        if (userId.equals(StpUtil.getLoginIdAsString())) {
            throw new BadRequestException("Can not follow yourself!");
        }
        Followed followed = new Followed();
        followed.setUserId(userId);
        followed.setFollowedUserId(followingUserId);
        followed.setCreateTime(new Date());
        baseMapper.insert(followed);
        systemNoticeHandle.sendNotice(userId, MessageConstant.MESSAGE_WATCH_NOTICE, MessageConstant.SYSTEM_MESSAGE_CODE, null, null, null);
    }

    @Override
    public void deleteFollowed(String userId, String followingUserId) {
        if (StringUtils.isBlank(followingUserId)) {
            throw new BadRequestException("Followed user id is empty!");
        }
        LambdaQueryWrapper<Followed> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Followed::getUserId, userId)
                .eq(Followed::getFollowedUserId, followingUserId);
        baseMapper.delete(queryWrapper);
    }
}
