package com.helloscala.service.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.web.exception.ConflictException;
import com.helloscala.common.web.exception.NotFoundException;
import com.helloscala.service.entity.FriendLink;
import com.helloscala.service.enums.DataEventEnum;
import com.helloscala.service.enums.FriendLinkEnum;
import com.helloscala.service.mapper.FriendLinkMapper;
import com.helloscala.service.service.FriendLinkService;
import com.helloscala.service.service.event.DataEventPublisherService;
import com.helloscala.service.web.request.CreateFriendLinkRequest;
import com.helloscala.service.web.request.UpdateFriendLinkRequest;
import com.helloscala.service.web.view.FriendLinkView;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink> implements FriendLinkService {

    private final DataEventPublisherService dataEventPublisherService;

    @Override
    public Page<FriendLinkView> selectFriendLinkPage(Page<?> page, String name, Integer status) {
        LambdaQueryWrapper<FriendLink> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), FriendLink::getName, name)
                .eq(status != null, FriendLink::getStatus, status)
                .orderByDesc(FriendLink::getSort);

        Page<FriendLink> friendLinkPage = baseMapper.selectPage(PageHelper.of(page), queryWrapper);
        return PageHelper.convertTo(friendLinkPage, FriendLinkServiceImpl::buildFriendLinkView);
    }

    private static @NotNull FriendLinkView buildFriendLinkView(FriendLink friendLink) {
        FriendLinkView friendLinkView = new FriendLinkView();
        friendLinkView.setId(friendLink.getId());
        friendLinkView.setName(friendLink.getName());
        friendLinkView.setUrl(friendLink.getUrl());
        friendLinkView.setAvatar(friendLink.getAvatar());
        friendLinkView.setInfo(friendLink.getInfo());
        friendLinkView.setEmail(friendLink.getEmail());
        friendLinkView.setSort(friendLink.getSort());
        friendLinkView.setStatus(friendLink.getStatus());
        friendLinkView.setReason(friendLink.getReason());
        friendLinkView.setCreateTime(friendLink.getCreateTime());
        friendLinkView.setUpdateTime(friendLink.getUpdateTime());
        return friendLinkView;
    }

    @Override
    public List<FriendLinkView> listVisible() {
        LambdaQueryWrapper<FriendLink> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FriendLink::getStatus, FriendLinkEnum.UP.code)
                .orderByDesc(FriendLink::getSort);
        List<FriendLink> friendLinks = baseMapper.selectList(queryWrapper);
        return friendLinks.stream().map(FriendLinkServiceImpl::buildFriendLinkView).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createFriendLink(CreateFriendLinkRequest request) {
        FriendLink friendLink = new FriendLink();
        friendLink.setName(request.getName());
        friendLink.setUrl(request.getUrl());
        friendLink.setAvatar(request.getAvatar());
        friendLink.setInfo(request.getInfo());
        friendLink.setEmail(request.getEmail());
        friendLink.setSort(request.getSort());
        friendLink.setStatus(request.getStatus());
        friendLink.setReason(request.getReason());
        friendLink.setCreateTime(new Date());
        baseMapper.insert(friendLink);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFriendLink(UpdateFriendLinkRequest request) {
        FriendLink friendLink = getById(request.getId());
        if (Objects.isNull(friendLink)) {
            throw new NotFoundException("FriendLink not found, id={}", request.getId());
        }

        FriendLink friendLinkToUpdate = new FriendLink();
        friendLinkToUpdate.setId(request.getId());
        friendLinkToUpdate.setName(request.getName());
        friendLinkToUpdate.setUrl(request.getUrl());
        friendLinkToUpdate.setAvatar(request.getAvatar());
        friendLinkToUpdate.setInfo(request.getInfo());
        friendLinkToUpdate.setEmail(request.getEmail());
        friendLinkToUpdate.setSort(request.getSort());
        friendLinkToUpdate.setStatus(request.getStatus());
        friendLinkToUpdate.setReason(request.getReason());
        friendLinkToUpdate.setUpdateTime(new Date());
        int update = baseMapper.updateById(friendLinkToUpdate);
        if (update <= 0) {
            throw new ConflictException("Update FriendLink failed, friendLink={}", JSONObject.toJSON(friendLinkToUpdate));
        }
        dataEventPublisherService.publishData(DataEventEnum.EMAIL_SEND, friendLink);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFriendLink(Set<String> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        if (rows <= 0) {
            throw new ConflictException("Failed to delete links ids=[{}]", StrUtil.join(", ", ids));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void top(String id) {
        Integer sort = baseMapper.getMaxSort();
        baseMapper.top(id, sort + 1);
    }
}
