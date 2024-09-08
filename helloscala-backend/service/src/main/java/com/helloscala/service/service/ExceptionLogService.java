package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.ExceptionLog;
import com.helloscala.service.web.view.ExceptionLogView;

import java.util.Set;


public interface ExceptionLogService extends IService<ExceptionLog> {
    Page<ExceptionLogView> listByPage(Page<?> page);

    void deleteExceptionLog(Set<String> ids);
}
