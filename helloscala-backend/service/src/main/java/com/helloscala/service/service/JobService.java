package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.web.exception.TaskException;
import com.helloscala.service.entity.Job;
import com.helloscala.service.web.request.ChangeJobStatusRequest;
import com.helloscala.service.web.request.CreateJobRequest;
import com.helloscala.service.web.request.UpdateJobRequest;
import com.helloscala.service.web.view.JobView;
import org.quartz.SchedulerException;

import java.util.List;
import java.util.Set;


public interface JobService extends IService<Job> {
    Page<JobView> selectJobPage(Page<?> page, String jobName, String jobGroup, String status);

    JobView selectJobById(String jobId);

    void addJob(CreateJobRequest request) throws SchedulerException, TaskException;

    void updateJob(UpdateJobRequest request) throws SchedulerException, TaskException;

    void deleteJob(Set<String> ids);

    void runJob(String id);

    void changeStatus(ChangeJobStatusRequest request) throws SchedulerException;

    void pauseJob(String id) throws SchedulerException;
}
