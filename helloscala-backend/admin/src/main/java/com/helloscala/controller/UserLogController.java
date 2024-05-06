package com.helloscala.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.service.UserLogService;
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
@RequestMapping("/system/userLog")
@RequiredArgsConstructor
@Tag(name = "用户日志管理")
public class
UserLogController {

    private final UserLogService userLogService;

    @GetMapping(value = "/list")
    @Operation(summary = "用户日志列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "用户日志列表")
    public ResponseResult selectUserLogPage() {
        return userLogService.selectUserLogPage();
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:userLog:delete")
    @OperationLogger(value = "删除用户日志")
    @Operation(summary = "删除用户日志", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "删除用户日志")
    public ResponseResult deleteBatch(@RequestBody List<Long> ids) {
        return userLogService.deleteUserLog(ids);
    }
}

