package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.Feedback;
import com.helloscala.service.web.request.CreateFeedbackRequest;
import com.helloscala.service.web.request.UpdateFeedbackRequest;
import com.helloscala.service.web.view.FeedbackView;

import java.util.Set;


public interface FeedbackService extends IService<Feedback> {

    Page<FeedbackView> listByPage(Page<?> page, Integer type);

    void deleteFeedback(Set<String> ids);

    void updateFeedback(UpdateFeedbackRequest request);

    void createFeedback(CreateFeedbackRequest request);
}
