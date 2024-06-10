package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.ExceptionLog;

import java.util.List;


public interface ExceptionLogService extends IService<ExceptionLog> {
    ResponseResult selectExceptionLogPage();

    ResponseResult deleteExceptionLog(List<Long> ids);
}
