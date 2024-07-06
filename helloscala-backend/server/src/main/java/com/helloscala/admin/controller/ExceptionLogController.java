package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.entity.ExceptionLog;
import com.helloscala.common.service.ExceptionLogService;
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
@RequestMapping("/system/exceptionLog")
@RequiredArgsConstructor
@Tag(name = "Exception log management")
public class ExceptionLogController {

    private final ExceptionLogService exceptionLogService;

    @GetMapping(value = "/list")
    @Operation(summary = "List exception logs", method = "GET")
    @ApiResponse(responseCode = "200", description = "List exception logs")
    public Response<Page<ExceptionLog>> selectExceptionLogPage() {
        Page<ExceptionLog> exceptionLogPage = exceptionLogService.selectExceptionLogPage();
        return ResponseHelper.ok(exceptionLogPage);
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:exceptionLog:delete")
    @OperationLogger(value = "Delete exception log")
    @Operation(summary = "Delete exception log", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete exception log")
    public EmptyResponse deleteExceptionLog(@RequestBody List<Long> ids) {
        exceptionLogService.deleteExceptionLog(ids);
        return ResponseHelper.ok();
    }
}

