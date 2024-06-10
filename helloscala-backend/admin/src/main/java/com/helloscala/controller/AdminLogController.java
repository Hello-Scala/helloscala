package com.helloscala.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.service.AdminLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/adminLog")
@RequiredArgsConstructor
@Tag(name = "Admin log")
public class AdminLogController {

    private final AdminLogService adminLogService;

    @GetMapping(value = "/list")
    @Operation(summary = "list admin log", method = "GET")
    @ApiResponse(responseCode = "200", description = "操作日志列表")
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

