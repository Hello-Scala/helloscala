package com.helloscala.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.service.entity.FeedBack;
import com.helloscala.service.mapper.FeedBackMapper;
import com.helloscala.service.service.FeedBackService;
import com.helloscala.common.utils.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class FeedBackServiceImpl extends ServiceImpl<FeedBackMapper, FeedBack> implements FeedBackService {

    @Override
    public Page<FeedBack> selectFeedBackPage(Integer type) {
        Page<FeedBack> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        LambdaQueryWrapper<FeedBack> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(FeedBack::getCreateTime)
                .eq(type != null, FeedBack::getType, type);
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFeedBack(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFeedBack(FeedBack feedBack) {
        baseMapper.updateById(feedBack);
    }
}
