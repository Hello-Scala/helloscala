package com.helloscala.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.service.ExceptionLogService;
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
@RequestMapping("/system/exceptionLog")
@RequiredArgsConstructor
@Tag(name = "异常日志管理")
public class ExceptionLogController {

    private final ExceptionLogService exceptionLogService;

    @GetMapping(value = "/list")
    @Operation(summary = "异常日志列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "异常日志列表")
    public ResponseResult selectExceptionLogPage() {
        return exceptionLogService.selectExceptionLogPage();
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:exceptionLog:delete")
    @OperationLogger(value = "删除异常日志")
    @Operation(summary = "删除异常日志", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "删除异常日志")
    public ResponseResult deleteExceptionLog(@RequestBody List<Long> ids) {
        return exceptionLogService.deleteExceptionLog(ids);
    }
}

