package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.FeedBack;

import java.util.List;


public interface FeedBackService extends IService<FeedBack> {

    Page<FeedBack> selectFeedBackPage(Integer type);

    void deleteFeedBack(List<Integer> ids);

    void updateFeedBack(FeedBack feedBack);
}
