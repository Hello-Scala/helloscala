package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.service.AdminLogService;
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
    public ResponseResult selectAdminLogPage() {
        return adminLogService.selectAdminLogPage();
    }

    @DeleteMapping(value = "/delete")
    @OperationLogger(value = "delete admin log")
    @SaCheckPermission("system:adminLog:delete")
    @Operation(summary = "delete admin operation log", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "delete admin operation log")
    public ResponseResult deleteAdminLog(@RequestBody List<Long> ids) {
        return adminLogService.deleteAdminLog(ids);
    }
}

