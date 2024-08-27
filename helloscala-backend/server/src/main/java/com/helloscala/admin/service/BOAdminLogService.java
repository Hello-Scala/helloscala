package com.helloscala.admin.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.view.BOAdminLogView;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.web.exception.ConflictException;
import com.helloscala.service.service.AdminLogService;
import com.helloscala.service.web.view.AdminLogView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Steve Zou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOAdminLogService {
    private final AdminLogService adminLogService;

    public Page<BOAdminLogView> selectAdminLogPage() {
        Page<?> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        Page<AdminLogView> adminLogPage = adminLogService.listByPage(page);

        return PageHelper.convertTo(adminLogPage, adminLog -> {
            BOAdminLogView boAdminLogView = new BOAdminLogView();
            boAdminLogView.setId(adminLog.getId());
            boAdminLogView.setUsername(adminLog.getUsername());
            boAdminLogView.setRequestUrl(adminLog.getRequestUrl());
            boAdminLogView.setType(adminLog.getType());
            boAdminLogView.setOperationName(adminLog.getOperationName());
            boAdminLogView.setIp(adminLog.getIp());
            boAdminLogView.setSource(adminLog.getSource());
            boAdminLogView.setParamsJson(adminLog.getParamsJson());
            boAdminLogView.setClassPath(adminLog.getClassPath());
            boAdminLogView.setMethodName(adminLog.getMethodName());
            boAdminLogView.setSpendTime(adminLog.getSpendTime());
            boAdminLogView.setCreateTime(adminLog.getCreateTime());
            return boAdminLogView;
        });
    }

    public void deleteAdminLog(String userId, List<Long> ids) {
        int rows = adminLogService.deleteAdminLog(ids);
        if (rows <= 0) {
            throw new ConflictException("Failed to delete admin logs, ids=[{}]!", StrUtil.join(", ", ids));
        }
        log.info("deleteAdminLog success, userId={}, delete adminLogIds=[{}]", userId, StrUtil.join(",", ids));
    }

}
