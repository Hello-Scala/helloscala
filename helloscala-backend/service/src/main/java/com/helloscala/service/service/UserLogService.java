package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.UserLog;

import java.util.List;


public interface UserLogService extends IService<UserLog> {
    Page<UserLog> selectUserLogPage();

    int deleteUserLog(List<Long> ids);
}
