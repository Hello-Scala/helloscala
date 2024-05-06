package com.helloscala.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.FeedBack;

import java.util.List;


public interface FeedBackService extends IService<FeedBack> {

    /**
     * 分页
     * @param type
     * @return
     */
    ResponseResult selectFeedBackPage(Integer type);

    /**
     * 删除
     * @param ids
     * @return
     */
    ResponseResult deleteFeedBack(List<Integer> ids);

    /**
     * 修改
     * @param feedBack
     * @return
     */
    ResponseResult updateFeedBack(FeedBack feedBack);
}
