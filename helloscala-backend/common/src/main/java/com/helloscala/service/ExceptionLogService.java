package com.helloscala.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.ExceptionLog;

import java.util.List;


public interface ExceptionLogService extends IService<ExceptionLog> {

    /**
     * 分页
     * @return
     */
    ResponseResult selectExceptionLogPage();

    /**
     * 删除
     * @param ids
     * @return
     */
    ResponseResult deleteExceptionLog(List<Long> ids);
}
