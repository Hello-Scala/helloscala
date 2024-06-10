package com.helloscala.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.FeedBack;
import com.helloscala.common.mapper.FeedBackMapper;
import com.helloscala.common.service.FeedBackService;
import com.helloscala.common.utils.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class FeedBackServiceImpl extends ServiceImpl<FeedBackMapper, FeedBack> implements FeedBackService {

    @Override
    public ResponseResult selectFeedBackPage(Integer type) {
        Page<FeedBack> feedBackPage = baseMapper.selectPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()), new LambdaQueryWrapper<FeedBack>()
                .orderByDesc(FeedBack::getCreateTime).eq(type != null,FeedBack::getType,type));
        return ResponseResult.success(feedBackPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteFeedBack(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateFeedBack(FeedBack feedBack) {
        baseMapper.updateById(feedBack);
        return ResponseResult.success();
    }
}
