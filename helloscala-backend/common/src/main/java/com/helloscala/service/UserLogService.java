package com.helloscala.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.UserLog;

import java.util.List;


public interface UserLogService extends IService<UserLog> {

    /**
     * 分页
     * @return
     */
    ResponseResult selectUserLogPage();

    /**
     * 删除
     * @param ids
     * @return
     */
    ResponseResult deleteUserLog(List<Long> ids);
}
