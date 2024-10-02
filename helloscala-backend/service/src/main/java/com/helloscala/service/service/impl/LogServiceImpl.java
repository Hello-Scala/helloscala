package com.helloscala.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.service.entity.UserLog;
import com.helloscala.service.mapper.UserLogMapper;
import com.helloscala.service.service.UserLogService;
import com.helloscala.service.web.view.UserIPCountView;
import com.helloscala.service.web.view.UserLogView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class LogServiceImpl extends ServiceImpl<UserLogMapper, UserLog> implements UserLogService {
    @Override
    public Page<UserLogView> selectUserLogPage(Page<?> page) {
        LambdaQueryWrapper<UserLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(UserLog::getCreateTime);
        Page<UserLog> userLogPage = baseMapper.selectPage(PageHelper.of(page), queryWrapper);
        return PageHelper.convertTo(userLogPage, userLog -> {
            UserLogView userLogView = new UserLogView();
            userLogView.setId(userLog.getId());
            userLogView.setIp(userLog.getIp());
            userLogView.setAddress(userLog.getAddress());
            userLogView.setType(userLog.getType());
            userLogView.setDescription(userLog.getDescription());
            userLogView.setModel(userLog.getModel());
            userLogView.setAccessOs(userLog.getAccessOs());
            userLogView.setClientType(userLog.getClientType());
            userLogView.setBrowser(userLog.getBrowser());
            userLogView.setCreateTime(userLog.getCreateTime());
            userLogView.setResult(userLog.getResult());
            return userLogView;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserLog(List<String> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public List<UserIPCountView> countIP(String date) {
        return baseMapper.countUserIP(date);
    }
}
