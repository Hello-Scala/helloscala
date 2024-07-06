package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.entity.AdminLog;

import java.util.List;


public interface AdminLogService extends IService<AdminLog> {
    Page<AdminLog> selectAdminLogPage();

    int deleteAdminLog(List<Long> ids);
}
