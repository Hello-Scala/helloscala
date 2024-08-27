package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.Job;
import com.helloscala.common.web.exception.TaskException;
import org.quartz.SchedulerException;

import java.util.List;


public interface JobService extends IService<Job> {
    Page<Job> selectJobPage(String jobName, String jobGroup, String status);

    Job selectJobById(Long jobId);

    void addJob(Job job) throws SchedulerException, TaskException;

    void updateJob(Job job) throws SchedulerException, TaskException;

    void deleteJob(List<Long> ids);

    void runJob(Job job);

    void changeStatus(Job job) throws SchedulerException;

    void pauseJob(Job job) throws SchedulerException;

}
