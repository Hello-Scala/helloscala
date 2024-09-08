package com.helloscala.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.web.exception.NotFoundException;
import com.helloscala.service.entity.FeedBack;
import com.helloscala.service.mapper.FeedBackMapper;
import com.helloscala.service.service.FeedBackService;
import com.helloscala.service.web.request.UpdateFeedbackRequest;
import com.helloscala.service.web.view.FeedbackView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;


@Service
public class FeedBackServiceImpl extends ServiceImpl<FeedBackMapper, FeedBack> implements FeedBackService {

    @Override
    public Page<FeedbackView> listByPage(Page<?> page, Integer type) {
        LambdaQueryWrapper<FeedBack> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(FeedBack::getCreateTime)
                .eq(Objects.nonNull(type), FeedBack::getType, type);
        Page<FeedBack> feedBackPage = baseMapper.selectPage(PageHelper.of(page), queryWrapper);
        return PageHelper.convertTo(feedBackPage, feedBack -> {
            FeedbackView feedBackView = new FeedbackView();
            feedBackView.setId(feedBack.getId());
            feedBackView.setUserId(feedBack.getUserId());
            feedBackView.setTitle(feedBack.getTitle());
            feedBackView.setContent(feedBack.getContent());
            feedBackView.setCreateTime(feedBack.getCreateTime());
            feedBackView.setImgUrl(feedBack.getImgUrl());
            feedBackView.setType(feedBack.getType());
            feedBackView.setStatus(feedBack.getStatus());
            return feedBackView;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFeedBack(Set<String> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFeedBack(UpdateFeedbackRequest request) {
        FeedBack feedBack = getById(request.getId());
        if (Objects.isNull(feedBack)) {
            throw new NotFoundException("Feedback not found, id={}", request.getId());
        }
        FeedBack feedBackToUpdate = new FeedBack();
        feedBackToUpdate.setId(request.getId());
        feedBackToUpdate.setUserId(request.getUserId());
        feedBackToUpdate.setTitle(request.getTitle());
        feedBackToUpdate.setContent(request.getContent());
        feedBackToUpdate.setImgUrl(request.getImgUrl());
        feedBackToUpdate.setType(request.getType());
        feedBackToUpdate.setStatus(request.getStatus());
        baseMapper.updateById(feedBackToUpdate);
    }
}
