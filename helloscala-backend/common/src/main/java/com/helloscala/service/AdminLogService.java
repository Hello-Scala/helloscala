package com.helloscala.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.AdminLog;

import java.util.List;


public interface AdminLogService extends IService<AdminLog> {

    /**
     * 分页查询操作日志
     * @return
     */
    ResponseResult selectAdminLogPage();

    /**
     * 批量删除操作日志
     * @param ids 操作日志id集合
     * @return
     */
    ResponseResult deleteAdminLog(List<Long> ids);
}
