package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.UserLog;
import com.helloscala.service.web.view.UserIPCountView;
import com.helloscala.service.web.view.UserLogView;

import java.util.List;


public interface UserLogService extends IService<UserLog> {
    Page<UserLogView> selectUserLogPage(Page<?> page);

    void deleteUserLog(List<String> ids);

    List<UserIPCountView> countIP(String date);
}
