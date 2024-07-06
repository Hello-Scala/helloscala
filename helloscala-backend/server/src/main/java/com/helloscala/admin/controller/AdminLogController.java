package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.entity.AdminLog;
import com.helloscala.common.service.AdminLogService;
import com.helloscala.common.web.exception.ConflictException;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/adminLog")
@RequiredArgsConstructor
@Tag(name = "Admin log")
public class AdminLogController {

    private final AdminLogService adminLogService;

    @GetMapping(value = "/list")
    @Operation(summary = "list admin operation log", method = "GET")
    @ApiResponse(responseCode = "200", description = "admin operation log list")
    public Response<Page<AdminLog>> selectAdminLogPage() {
        Page<AdminLog> adminLogPage = adminLogService.selectAdminLogPage();
        return ResponseHelper.ok(adminLogPage);
    }

    @DeleteMapping(value = "/delete")
    @OperationLogger(value = "delete admin log")
    @SaCheckPermission("system:adminLog:delete")
    @Operation(summary = "delete admin operation log", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "delete admin operation log")
    public EmptyResponse deleteAdminLog(@RequestBody List<Long> ids) {
        int rows = adminLogService.deleteAdminLog(ids);
        if (rows > 0) {
            return ResponseHelper.ok();
        } else {
            throw new ConflictException("Failed to delete admin logs, ids=[{}]!", StrUtil.join(", ", ids));
        }
    }
}

