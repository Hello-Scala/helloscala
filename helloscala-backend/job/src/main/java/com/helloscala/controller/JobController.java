package com.helloscala.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Job;
import com.helloscala.enums.TaskException;
import com.helloscala.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/system/job")
@Tag(name = "定时任务管理")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping(value = "/list")
    @Operation(summary = "定时任务列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "定时任务列表")
    public ResponseResult selectJobPage(@RequestParam(name = "jobName", required = false) String jobName,
                                        @RequestParam(name = "jobGroup", required = false) String jobGroup,
                                        @RequestParam(name = "status", required = false) String status) {
        return jobService.selectJobPage(jobName,jobGroup,status);
    }

    @GetMapping(value = "/info")
    @Operation(summary = "定时任务详情", method = "GET")
    @ApiResponse(responseCode = "200", description = "定时任务详情")
    public ResponseResult selectJobById(@RequestParam(name = "jobId", required = true) Long jobId) {
        return jobService.selectJobById(jobId);
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("system:job:add")
    @Operation(summary = "添加定时任务", method = "POST")
    @ApiResponse(responseCode = "200", description = "添加定时任务")
    @OperationLogger(value = "添加定时任务")
    public ResponseResult addJob(@RequestBody Job job) throws SchedulerException, TaskException {
        return jobService.addJob(job);
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("system:job:update")
    @Operation(summary = "修改定时任务", method = "PUT")
    @ApiResponse(responseCode = "200", description = "修改定时任务")
    @OperationLogger(value = "修改定时任务")
    public ResponseResult updateJob(@RequestBody Job job) throws SchedulerException, TaskException {
        return jobService.updateJob(job);
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:job:delete")
    @Operation(summary = "批量删除定时任务", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "批量删除定时任务")
    @OperationLogger(value = "批量删除定时任务")
    public ResponseResult deleteJob(@RequestBody List<Long> ids) {
        return jobService.deleteJob(ids);
    }

    @PostMapping(value = "/run")
    @SaCheckPermission("system:job:run")
    @Operation(summary = "立即执行", method = "POST")
    @ApiResponse(responseCode = "200", description = "立即执行")
    @OperationLogger(value = "立即执行")
    public ResponseResult runJob(@RequestBody Job job) {
        return jobService.runJob(job);
    }

    @PostMapping(value = "/change")
    @SaCheckPermission("system:job:change")
    @Operation(summary = "修改状态", method = "POST")
    @ApiResponse(responseCode = "200", description = "修改状态")
    @OperationLogger(value = "修改状态")
    public ResponseResult changeStatus(@RequestBody Job job) throws SchedulerException {
        return jobService.changeStatus(job);
    }
}

