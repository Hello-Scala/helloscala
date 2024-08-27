package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.AdminLog;
import com.helloscala.service.web.view.AdminLogView;

import java.util.List;


public interface AdminLogService extends IService<AdminLog> {
    Page<AdminLogView> listByPage(Page<?> page);

    int deleteAdminLog(List<Long> ids);
}
