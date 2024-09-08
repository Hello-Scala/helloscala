package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.FeedBack;
import com.helloscala.service.web.request.UpdateFeedbackRequest;
import com.helloscala.service.web.view.FeedbackView;

import java.util.Set;


public interface FeedBackService extends IService<FeedBack> {

    Page<FeedbackView> listByPage(Page<?> page, Integer type);

    void deleteFeedBack(Set<String> ids);

    void updateFeedBack(UpdateFeedbackRequest request);
}
