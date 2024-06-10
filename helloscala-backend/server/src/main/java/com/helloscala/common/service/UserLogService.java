package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.UserLog;

import java.util.List;


public interface UserLogService extends IService<UserLog> {
    ResponseResult selectUserLogPage();

    ResponseResult deleteUserLog(List<Long> ids);
}
