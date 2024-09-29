package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.view.BOUserLogView;
import com.helloscala.admin.service.BOUserLogService;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
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
@Tag(name = "User log management")
public class
UserLogController {

    private final BOUserLogService userLogService;

    @GetMapping(value = "/list")
    @Operation(summary = "List user log", method = "GET")
    @ApiResponse(responseCode = "200", description = "List user log")
    public Response<Page<BOUserLogView>> selectUserLogPage() {
        Page<BOUserLogView> userLogPage = userLogService.listByPage();
        return ResponseHelper.ok(userLogPage);
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:userLog:delete")
    @OperationLogger(value = "Delete use log")
    @Operation(summary = "Delete use log", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete use log")
    public EmptyResponse deleteBatch(@RequestBody List<String> ids) {
        String userId = StpUtil.getLoginIdAsString();
        userLogService.deleteBatch(userId, ids);
        return ResponseHelper.ok();
    }
}

