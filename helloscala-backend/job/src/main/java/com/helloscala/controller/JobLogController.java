package com.helloscala.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.ResponseResult;
import com.helloscala.service.JobLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/system/jobLog")
@Tag(name = "定时任务调度日志管理")
@RequiredArgsConstructor
public class JobLogController {

    private final JobLogService jobLogService;

    @GetMapping(value = "/list")
    @Operation(summary = "定时任务日志列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "定时任务日志列表")
    public ResponseResult selectJobLogPage(
        @RequestParam(name = "jobName", required = false) String jobName,
        @RequestParam(name = "jobGroup", required = false) String jobGroup,
        @RequestParam(name = "status", required = false) String status,
        @RequestParam(name = "startTime", required = false) String startTime,
        @RequestParam(name = "endTime", required = false) String endTime,
        @RequestParam(name = "jobId", required = false) Long jobId) {
        return jobLogService.selectJobLogPage(jobName,jobGroup,status,startTime,endTime,jobId);
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:jobLog:delete")
    @Operation(summary = "批量删除日志列表", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "批量删除日志列表")
    public ResponseResult deleteBatch(@RequestBody List<Long> ids) {
        return jobLogService.deleteJobLog(ids);
    }

    @GetMapping(value = "/clean")
    @SaCheckPermission("system:jobLog:clean")
    @Operation(summary = "清空日志列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "清空日志列表")
    public ResponseResult clean() {
        return jobLogService.cleanJobLog();
    }
}

