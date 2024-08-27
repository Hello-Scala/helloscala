package com.helloscala.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.service.entity.UserLog;
import com.helloscala.service.mapper.UserLogMapper;
import com.helloscala.service.service.UserLogService;
import com.helloscala.common.utils.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class LogServiceImpl extends ServiceImpl<UserLogMapper, UserLog> implements UserLogService {
    @Override
    public Page<UserLog> selectUserLogPage() {
        Page<UserLog> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        LambdaQueryWrapper<UserLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(UserLog::getCreateTime);
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserLog(List<Long> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        return rows;
    }
}
