package com.helloscala.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.request.BOChangeJobStatusRequest;
import com.helloscala.admin.controller.request.BOCreateJobRequest;
import com.helloscala.admin.controller.request.BORunJobRequest;
import com.helloscala.admin.controller.request.BOUpdateJobRequest;
import com.helloscala.admin.controller.view.BOJobView;
import com.helloscala.admin.service.BOJobService;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.entity.Job;
import com.helloscala.common.web.exception.TaskException;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;


@RestController
@RequestMapping("/system/job")
@Tag(name = "Job management")
@RequiredArgsConstructor
public class JobController {

    private final BOJobService jobService;

    @GetMapping(value = "/list")
    @Operation(summary = "List scheduled job", method = "GET")
    @ApiResponse(responseCode = "200", description = "List scheduled job")
    public Response<Page<BOJobView>> selectJobPage(@RequestParam(name = "jobName", required = false) String jobName,
                                                   @RequestParam(name = "jobGroup", required = false) String jobGroup,
                                                   @RequestParam(name = "status", required = false) String status) {
        Page<BOJobView> jobPage = jobService.listByPage(jobName, jobGroup, status);
        return ResponseHelper.ok(jobPage);
    }

    @GetMapping(value = "/info")
    @Operation(summary = "Get scheduled job detail", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get scheduled job detail")
    public Response<BOJobView> selectJobById(@RequestParam(name = "jobId", required = true) String jobId) {
        BOJobView job = jobService.get(jobId);
        return ResponseHelper.ok(job);
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("system:job:add")
    @Operation(summary = "Schedule new job", method = "POST")
    @ApiResponse(responseCode = "200", description = "Schedule new job")
    @OperationLogger(value = "Schedule new job")
    public EmptyResponse create(@RequestBody BOCreateJobRequest request) throws SchedulerException, TaskException {
        String userId = StpUtil.getLoginIdAsString();
        jobService.create(userId, request);
        return ResponseHelper.ok();
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("system:job:update")
    @Operation(summary = "Update scheduled job", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update scheduled job")
    @OperationLogger(value = "Update scheduled job")
    public EmptyResponse update(@RequestBody BOUpdateJobRequest request) throws SchedulerException, TaskException {
        String userId = StpUtil.getLoginIdAsString();
        jobService.update(userId, request);
        return ResponseHelper.ok();
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:job:delete")
    @Operation(summary = "Bulk delete scheduled job", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Bulk delete scheduled job")
    @OperationLogger(value = "Bulk delete scheduled job")
    public EmptyResponse deleteJob(@RequestBody List<String> ids) {
        String userId = StpUtil.getLoginIdAsString();
        jobService.delete(userId, new HashSet<>(ids));
        return ResponseHelper.ok();
    }

    @PostMapping(value = "/run")
    @SaCheckPermission("system:job:run")
    @Operation(summary = "Run job", method = "POST")
    @ApiResponse(responseCode = "200", description = "Run job")
    @OperationLogger(value = "Run job")
    public EmptyResponse run(@RequestBody BORunJobRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        jobService.run(userId, request.getJobId());
        return ResponseHelper.ok();
    }

    @PostMapping(value = "/change")
    @SaCheckPermission("system:job:change")
    @Operation(summary = "Change job status", method = "POST")
    @ApiResponse(responseCode = "200", description = "Change job status")
    @OperationLogger(value = "Change job status")
    public EmptyResponse changeStatus(@RequestBody BOChangeJobStatusRequest request) throws SchedulerException {
        String userId = StpUtil.getLoginIdAsString();
        jobService.changeStatus(userId, request);
        return ResponseHelper.ok();
    }
}