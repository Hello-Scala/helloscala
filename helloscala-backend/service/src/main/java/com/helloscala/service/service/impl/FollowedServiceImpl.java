package com.helloscala.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.service.entity.Collect;
import com.helloscala.service.entity.Followed;
import com.helloscala.service.mapper.CollectMapper;
import com.helloscala.service.mapper.FollowedMapper;
import com.helloscala.service.service.CollectService;
import com.helloscala.service.service.FollowedService;
import com.helloscala.service.web.view.CollectCountView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author Steve Zou
 */
@Service
@RequiredArgsConstructor
public class FollowedServiceImpl extends ServiceImpl<FollowedMapper, Followed> implements FollowedService {

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
}
