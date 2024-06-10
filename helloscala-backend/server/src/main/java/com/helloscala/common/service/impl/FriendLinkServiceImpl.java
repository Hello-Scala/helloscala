package com.helloscala.common.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.FriendLink;
import com.helloscala.common.enums.DataEventEnum;
import com.helloscala.common.event.DataEventPublisherService;
import com.helloscala.common.mapper.FriendLinkMapper;
import com.helloscala.common.service.EmailService;
import com.helloscala.common.service.FriendLinkService;
import com.helloscala.common.utils.PageUtil;
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
    public ResponseResult selectFriendLinkPage(String name, Integer status) {
        LambdaQueryWrapper<FriendLink> queryWrapper= new LambdaQueryWrapper<FriendLink>()
                .like(StringUtils.isNotBlank(name), FriendLink::getName,name)
                .eq(status != null, FriendLink::getStatus,status).orderByDesc(FriendLink::getSort);

        Page<FriendLink> friendLinkPage = baseMapper.selectPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),queryWrapper);
        return ResponseResult.success(friendLinkPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addFriendLink(FriendLink friendLink) {
        baseMapper.insert(friendLink);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateFriendLink(FriendLink friendLink) {
        baseMapper.updateById(friendLink);
        dataEventPublisherService.publishData(DataEventEnum.EMAIL_SEND,friendLink);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteFriendLink(List<Integer> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ? ResponseResult.success(): ResponseResult.error(StrUtil.format("Failed to delete links ids=[{}]", StrUtil.join(", ", ids)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult top(Integer id) {
        Integer sort = baseMapper.getMaxSort();
        baseMapper.top(id,sort+1);
        return ResponseResult.success();
    }
}
