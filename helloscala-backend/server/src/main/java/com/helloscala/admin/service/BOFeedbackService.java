package com.helloscala.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.request.BOUpdateFeedbackRequest;
import com.helloscala.admin.controller.view.BOFeedbackView;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.service.service.FeedbackService;
import com.helloscala.service.web.request.UpdateFeedbackRequest;
import com.helloscala.service.web.view.FeedbackView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BOFeedbackService {
    private final FeedbackService feedBackService;

    public Page<BOFeedbackView> listByPage(Integer type) {
        Page<?> page = PageUtil.getPage();
        Page<FeedbackView> feedBackViewPage = feedBackService.listByPage(page, type);
        return PageHelper.convertTo(feedBackViewPage, feedbackView -> {
            BOFeedbackView boFeedbackView = new BOFeedbackView();
            boFeedbackView.setId(feedbackView.getId());
            boFeedbackView.setUserId(feedbackView.getUserId());
            boFeedbackView.setTitle(feedbackView.getTitle());
            boFeedbackView.setContent(feedbackView.getContent());
            boFeedbackView.setCreateTime(feedbackView.getCreateTime());
            boFeedbackView.setImgUrl(feedbackView.getImgUrl());
            boFeedbackView.setType(feedbackView.getType());
            boFeedbackView.setStatus(feedbackView.getStatus());
            return boFeedbackView;
        });
    }

    public void deleteBatch(String userId, Set<String> ids) {
        feedBackService.deleteFeedback(ids);
        log.info("userId={}, deleted feedback ids=[{}]", userId, String.join(",", ids));
    }

    public void update(String userId, BOUpdateFeedbackRequest request) {
        UpdateFeedbackRequest updateFeedbackRequest = new UpdateFeedbackRequest();
        updateFeedbackRequest.setId(request.getId());
        updateFeedbackRequest.setUserId(request.getUserId());
        updateFeedbackRequest.setTitle(request.getTitle());
        updateFeedbackRequest.setContent(request.getContent());
        updateFeedbackRequest.setImgUrl(request.getImgUrl());
        updateFeedbackRequest.setType(request.getType());
        updateFeedbackRequest.setStatus(request.getStatus());
        updateFeedbackRequest.setRequestBy(userId);
        feedBackService.updateFeedback(updateFeedbackRequest);
    }
}
