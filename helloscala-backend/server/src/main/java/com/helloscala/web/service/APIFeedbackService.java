package com.helloscala.web.service;

import com.helloscala.service.service.FeedbackService;
import com.helloscala.service.web.request.CreateFeedbackRequest;
import com.helloscala.web.controller.feedback.APICreateFeedbackRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class APIFeedbackService {
    private final FeedbackService feedbackService;

    public void create(String userId, APICreateFeedbackRequest request) {
        CreateFeedbackRequest createFeedbackRequest = new CreateFeedbackRequest();
        createFeedbackRequest.setUserId(request.getUserId());
        createFeedbackRequest.setTitle(request.getTitle());
        createFeedbackRequest.setContent(request.getContent());
        createFeedbackRequest.setImgUrl(request.getImgUrl());
        createFeedbackRequest.setType(request.getType());
        createFeedbackRequest.setStatus(request.getStatus());
        createFeedbackRequest.setRequestBy(userId);
        feedbackService.createFeedback(createFeedbackRequest);
    }
}
