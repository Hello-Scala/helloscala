package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.entity.UserLog;

import java.util.List;


public interface UserLogService extends IService<UserLog> {
    Page<UserLog> selectUserLogPage();

    int deleteUserLog(List<Long> ids);
}
