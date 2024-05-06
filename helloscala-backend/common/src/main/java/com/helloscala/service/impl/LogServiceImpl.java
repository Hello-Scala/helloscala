package com.helloscala.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.UserLog;
import com.helloscala.mapper.UserLogMapper;
import com.helloscala.service.UserLogService;
import com.helloscala.utils.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class LogServiceImpl extends ServiceImpl<UserLogMapper, UserLog> implements UserLogService {

    /**
     * 用户日志列表
     * @return
     */
    @Override
    public ResponseResult selectUserLogPage() {
        Page<UserLog> sysLogPage = baseMapper.selectPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),new LambdaQueryWrapper<UserLog>()
                .orderByDesc(UserLog::getCreateTime));
        return ResponseResult.success(sysLogPage);
    }

    /**
     * 批量删除用户日志
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteUserLog(List<Long> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }
}
