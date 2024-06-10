package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.FeedBack;

import java.util.List;


public interface FeedBackService extends IService<FeedBack> {

    ResponseResult selectFeedBackPage(Integer type);

    ResponseResult deleteFeedBack(List<Integer> ids);

    ResponseResult updateFeedBack(FeedBack feedBack);
}
