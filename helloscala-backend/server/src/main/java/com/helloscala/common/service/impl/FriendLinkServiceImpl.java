package com.helloscala.common.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.entity.FriendLink;
import com.helloscala.common.enums.DataEventEnum;
import com.helloscala.common.event.DataEventPublisherService;
import com.helloscala.common.mapper.FriendLinkMapper;
import com.helloscala.common.service.FriendLinkService;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.web.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink> implements FriendLinkService {

    private final DataEventPublisherService dataEventPublisherService;

    @Override
    public Page<FriendLink> selectFriendLinkPage(String name, Integer status) {
        LambdaQueryWrapper<FriendLink> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), FriendLink::getName, name)
                .eq(status != null, FriendLink::getStatus, status)
                .orderByDesc(FriendLink::getSort);

        Page<FriendLink> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFriendLink(FriendLink friendLink) {
        baseMapper.insert(friendLink);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFriendLink(FriendLink friendLink) {
        baseMapper.updateById(friendLink);
        dataEventPublisherService.publishData(DataEventEnum.EMAIL_SEND, friendLink);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFriendLink(List<Integer> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        if (rows <= 0) {
            throw new ConflictException("Failed to delete links ids=[{}]", StrUtil.join(", ", ids));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void top(Integer id) {
        Integer sort = baseMapper.getMaxSort();
        baseMapper.top(id, sort + 1);
    }
}
