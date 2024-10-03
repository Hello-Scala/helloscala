package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.request.BOSearchJobLogRequest;
import com.helloscala.admin.controller.view.BOJobLogView;
import com.helloscala.admin.service.BOJobLogService;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;


@RestController
@RequestMapping("/system/jobLog")
@Tag(name = "Job log management")
@RequiredArgsConstructor
public class JobLogController {
    private final BOJobLogService jobLogService;

    @GetMapping(value = "/list")
    @Operation(summary = "List job log", method = "GET")
    @ApiResponse(responseCode = "200", description = "List job log")
    public Response<Page<BOJobLogView>> selectJobLogPage(
            @RequestParam(name = "jobName", required = false) String jobName,
            @RequestParam(name = "jobGroup", required = false) String jobGroup,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "startTime", required = false) String startTime,
            @RequestParam(name = "endTime", required = false) String endTime,
            @RequestParam(name = "jobId", required = false) Long jobId) {
        BOSearchJobLogRequest request = new BOSearchJobLogRequest();
        request.setJobId(jobId);
        request.setJobName(jobName);
        request.setJobGroup(jobGroup);
        request.setStatus(status);
        request.setEndTime(endTime);
        request.setStartTime(startTime);
        Page<BOJobLogView> jobLogViewPage = jobLogService.search(request);
        return ResponseHelper.ok(jobLogViewPage);
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:jobLog:delete")
    @Operation(summary = "Bulk delete job log", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Bulk delete job log")
    public EmptyResponse deleteBatch(@RequestBody List<String> ids) {
        String userId = StpUtil.getLoginIdAsString();
        jobLogService.bulkDelete(userId, new HashSet<>(ids));
        return ResponseHelper.ok();
    }

    @GetMapping(value = "/clean")
    @SaCheckPermission("system:jobLog:clean")
    @Operation(summary = "Clear job log", method = "GET")
    @ApiResponse(responseCode = "200", description = "Clear job log")
    public EmptyResponse clean() {
        String userId = StpUtil.getLoginIdAsString();
        jobLogService.clean(userId);
        return ResponseHelper.ok();
    }
}