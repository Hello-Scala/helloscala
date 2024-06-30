package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.entity.ExceptionLog;

import java.util.List;


public interface ExceptionLogService extends IService<ExceptionLog> {
    Page<ExceptionLog> selectExceptionLogPage();

    void deleteExceptionLog(List<Long> ids);
}
