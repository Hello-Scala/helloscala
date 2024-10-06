package com.helloscala.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.web.exception.ConflictException;
import com.helloscala.common.web.exception.NotFoundException;
import com.helloscala.service.entity.Feedback;
import com.helloscala.service.mapper.FeedbackMapper;
import com.helloscala.service.service.FeedbackService;
import com.helloscala.service.web.request.CreateFeedbackRequest;
import com.helloscala.service.web.request.UpdateFeedbackRequest;
import com.helloscala.service.web.view.FeedbackView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;
import java.util.Set;


@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {

    @Override
    public Page<FeedbackView> listByPage(Page<?> page, Integer type) {
        LambdaQueryWrapper<Feedback> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Feedback::getCreateTime)
                .eq(Objects.nonNull(type), Feedback::getType, type);
        Page<Feedback> feedBackPage = baseMapper.selectPage(PageHelper.of(page), queryWrapper);
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
    public void deleteFeedback(Set<String> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFeedback(UpdateFeedbackRequest request) {
        Feedback feedBack = getById(request.getId());
        if (Objects.isNull(feedBack)) {
            throw new NotFoundException("Feedback not found, id={}", request.getId());
        }
        Feedback feedbackToUpdate = new Feedback();
        feedbackToUpdate.setId(request.getId());
        feedbackToUpdate.setUserId(request.getUserId());
        feedbackToUpdate.setTitle(request.getTitle());
        feedbackToUpdate.setContent(request.getContent());
        feedbackToUpdate.setImgUrl(request.getImgUrl());
        feedbackToUpdate.setType(request.getType());
        feedbackToUpdate.setStatus(request.getStatus());
        baseMapper.updateById(feedbackToUpdate);
    }

    @Override
    public void createFeedback(CreateFeedbackRequest request) {
        Feedback feedback = new Feedback();
        feedback.setUserId(request.getUserId());
        feedback.setTitle(request.getTitle());
        feedback.setContent(request.getContent());
        feedback.setImgUrl(request.getImgUrl());
        feedback.setType(request.getType());
        feedback.setStatus(request.getStatus());
        feedback.setCreateTime(new Date());
        int insert = baseMapper.insert(feedback);
        if (insert <= 0) {
            throw new ConflictException("Failed to add feedback!");
        }
    }
}
