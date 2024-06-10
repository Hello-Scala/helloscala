package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.AdminLog;

import java.util.List;


public interface AdminLogService extends IService<AdminLog> {
    ResponseResult selectAdminLogPage();

    ResponseResult deleteAdminLog(List<Long> ids);
}
