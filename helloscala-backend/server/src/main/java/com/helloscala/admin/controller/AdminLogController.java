package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.view.BOAdminLogView;
import com.helloscala.admin.service.BOAdminLogService;
import com.helloscala.common.annotation.OperationLogger;
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
    private final BOAdminLogService adminLogService;

    @GetMapping(value = "/list")
    @Operation(summary = "list admin operation log", method = "GET")
    @ApiResponse(responseCode = "200", description = "admin operation log list")
    public Response<Page<BOAdminLogView>> selectAdminLogPage() {
        Page<BOAdminLogView> adminLogPage = adminLogService.selectAdminLogPage();
        return ResponseHelper.ok(adminLogPage);
    }

    @DeleteMapping(value = "/delete")
    @OperationLogger(value = "delete admin log")
    @SaCheckPermission("system:adminLog:delete")
    @Operation(summary = "delete admin operation log", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "delete admin operation log")
    public EmptyResponse deleteAdminLog(@RequestBody List<Long> ids) {
        String userId = StpUtil.getLoginIdAsString();
        adminLogService.deleteAdminLog(userId, ids);
        return ResponseHelper.ok();
    }
}

