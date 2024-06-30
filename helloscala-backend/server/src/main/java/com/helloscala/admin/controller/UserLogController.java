package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.entity.UserLog;
import com.helloscala.common.service.UserLogService;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/userLog")
@RequiredArgsConstructor
@Tag(name = "User log management")
public class
UserLogController {

    private final UserLogService userLogService;

    @GetMapping(value = "/list")
    @Operation(summary = "List user log", method = "GET")
    @ApiResponse(responseCode = "200", description = "List user log")
    public Response<Page<UserLog>> selectUserLogPage() {
        Page<UserLog> userLogPage = userLogService.selectUserLogPage();
        return ResponseHelper.ok(userLogPage);
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:userLog:delete")
    @OperationLogger(value = "Delete use log")
    @Operation(summary = "Delete use log", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete use log")
    public Response<Integer> deleteBatch(@RequestBody List<Long> ids) {
        int deleted = userLogService.deleteUserLog(ids);
        return ResponseHelper.ok(deleted);
    }
}

