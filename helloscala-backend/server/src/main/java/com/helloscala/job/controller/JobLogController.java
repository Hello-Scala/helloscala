package com.helloscala.job.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.ResponseResult;
import com.helloscala.job.service.JobLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/system/jobLog")
@Tag(name = "Job log management")
@RequiredArgsConstructor
public class JobLogController {

    private final JobLogService jobLogService;

    @GetMapping(value = "/list")
    @Operation(summary = "List job log", method = "GET")
    @ApiResponse(responseCode = "200", description = "List job log")
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
    @Operation(summary = "Bulk delete job log", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Bulk delete job log")
    public ResponseResult deleteBatch(@RequestBody List<Long> ids) {
        return jobLogService.deleteJobLog(ids);
    }

    @GetMapping(value = "/clean")
    @SaCheckPermission("system:jobLog:clean")
    @Operation(summary = "Clear job log", method = "GET")
    @ApiResponse(responseCode = "200", description = "Clear job log")
    public ResponseResult clean() {
        return jobLogService.cleanJobLog();
    }
}