package com.helloscala.job.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Job;
import com.helloscala.common.enums.TaskException;
import com.helloscala.job.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/system/job")
@Tag(name = "Job management")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping(value = "/list")
    @Operation(summary = "List scheduled job", method = "GET")
    @ApiResponse(responseCode = "200", description = "List scheduled job")
    public ResponseResult selectJobPage(@RequestParam(name = "jobName", required = false) String jobName,
                                        @RequestParam(name = "jobGroup", required = false) String jobGroup,
                                        @RequestParam(name = "status", required = false) String status) {
        return jobService.selectJobPage(jobName,jobGroup,status);
    }

    @GetMapping(value = "/info")
    @Operation(summary = "Get scheduled job detail", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get scheduled job detail")
    public ResponseResult selectJobById(@RequestParam(name = "jobId", required = true) Long jobId) {
        return jobService.selectJobById(jobId);
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("system:job:add")
    @Operation(summary = "Schedule new job", method = "POST")
    @ApiResponse(responseCode = "200", description = "Schedule new job")
    @OperationLogger(value = "Schedule new job")
    public ResponseResult addJob(@RequestBody Job job) throws SchedulerException, TaskException {
        return jobService.addJob(job);
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("system:job:update")
    @Operation(summary = "Update scheduled job", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update scheduled job")
    @OperationLogger(value = "Update scheduled job")
    public ResponseResult updateJob(@RequestBody Job job) throws SchedulerException, TaskException {
        return jobService.updateJob(job);
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:job:delete")
    @Operation(summary = "Bulk delete scheduled job", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Bulk delete scheduled job")
    @OperationLogger(value = "Bulk delete scheduled job")
    public ResponseResult deleteJob(@RequestBody List<Long> ids) {
        return jobService.deleteJob(ids);
    }

    @PostMapping(value = "/run")
    @SaCheckPermission("system:job:run")
    @Operation(summary = "Run job", method = "POST")
    @ApiResponse(responseCode = "200", description = "Run job")
    @OperationLogger(value = "Run job")
    public ResponseResult runJob(@RequestBody Job job) {
        return jobService.runJob(job);
    }

    @PostMapping(value = "/change")
    @SaCheckPermission("system:job:change")
    @Operation(summary = "Change job status", method = "POST")
    @ApiResponse(responseCode = "200", description = "Change job status")
    @OperationLogger(value = "Change job status")
    public ResponseResult changeStatus(@RequestBody Job job) throws SchedulerException {
        return jobService.changeStatus(job);
    }
}