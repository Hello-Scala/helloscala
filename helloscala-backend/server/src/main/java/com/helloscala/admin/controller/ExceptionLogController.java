package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.service.ExceptionLogService;
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
    public ResponseResult selectExceptionLogPage() {
        return exceptionLogService.selectExceptionLogPage();
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:exceptionLog:delete")
    @OperationLogger(value = "Delete exception log")
    @Operation(summary = "Delete exception log", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete exception log")
    public ResponseResult deleteExceptionLog(@RequestBody List<Long> ids) {
        return exceptionLogService.deleteExceptionLog(ids);
    }
}

