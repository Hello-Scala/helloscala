package com.helloscala.service.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.web.exception.NotFoundException;
import com.helloscala.service.entity.Say;
import com.helloscala.service.enums.PublishEnum;
import com.helloscala.service.mapper.SayMapper;
import com.helloscala.service.service.SayService;
import com.helloscala.service.web.request.CreateSayRequest;
import com.helloscala.service.web.request.UpdateSayRequest;
import com.helloscala.service.web.view.SayView;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class SayServiceImpl extends ServiceImpl<SayMapper, Say> implements SayService {

    @Override
    public Page<SayView> selectSayPage(Page<?> page, String keywords) {
        LambdaQueryWrapper<Say> sayLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StpUtil.hasRole(Constants.ADMIN_CODE)) {
            sayLambdaQueryWrapper.eq(Say::getIsPublic, PublishEnum.PUBLISH);
        }
        sayLambdaQueryWrapper.orderByDesc(Say::getCreateTime);
        Page<Say> sayPage = baseMapper.selectPage(PageHelper.of(page), sayLambdaQueryWrapper);
        return PageHelper.convertTo(sayPage, SayServiceImpl::buildSayView);
    }

    private static @NotNull SayView buildSayView(Say say) {
        SayView sayView = new SayView();
        sayView.setId(say.getId());
        sayView.setUserId(say.getUserId());
        sayView.setImgUrl(say.getImgUrl());
        sayView.setContent(say.getContent());
        sayView.setAddress(say.getAddress());
        sayView.setIsPublic(say.getIsPublic());
        sayView.setCreateTime(say.getCreateTime());
        sayView.setUpdateTime(say.getUpdateTime());
        return sayView;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSay(CreateSayRequest request) {
        Say say = new Say();
        say.setUserId(request.getUserId());
        say.setImgUrl(request.getImgUrl());
        say.setContent(request.getContent());
        say.setAddress(request.getAddress());
        say.setIsPublic(request.getIsPublic());
        say.setCreateTime(new Date());
        baseMapper.insert(say);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSay(Set<String> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public SayView selectSayById(String id) {
        Say say = baseMapper.selectById(id);
        return buildSayView(say);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSay(UpdateSayRequest request) {
        Say say = baseMapper.selectById(request.getId());
        if (Objects.isNull(say)) {
            throw new NotFoundException("Say not found, id={}!", request.getId());
        }

        say.setUserId(request.getUserId());
        say.setImgUrl(request.getImgUrl());
        say.setContent(request.getContent());
        say.setAddress(request.getAddress());
        say.setIsPublic(request.getIsPublic());
        say.setUpdateTime(new Date());
        baseMapper.updateById(say);
    }
}
