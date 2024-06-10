package com.helloscala.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Job;
import com.helloscala.common.enums.TaskException;
import org.quartz.SchedulerException;

import java.util.List;


public interface JobService extends IService<Job> {
    ResponseResult selectJobPage(String jobName, String jobGroup, String status);

    ResponseResult selectJobById(Long jobId);

    ResponseResult addJob(Job job) throws SchedulerException, TaskException;

    ResponseResult updateJob(Job job) throws SchedulerException, TaskException;

    ResponseResult deleteJob(List<Long> ids);

    ResponseResult runJob(Job job);

    ResponseResult changeStatus(Job job) throws SchedulerException;

    ResponseResult pauseJob(Job job) throws SchedulerException;

}
