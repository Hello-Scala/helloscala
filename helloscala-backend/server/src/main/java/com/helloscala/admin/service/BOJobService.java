package com.helloscala.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.request.BOChangeJobStatusRequest;
import com.helloscala.admin.controller.request.BOCreateJobRequest;
import com.helloscala.admin.controller.request.BOUpdateJobRequest;
import com.helloscala.admin.controller.view.BOJobView;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.web.exception.TaskException;
import com.helloscala.service.service.JobService;
import com.helloscala.service.web.request.ChangeJobStatusRequest;
import com.helloscala.service.web.request.CreateJobRequest;
import com.helloscala.service.web.request.UpdateJobRequest;
import com.helloscala.service.web.view.JobView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BOJobService {
    private final JobService jobService;

    public Page<BOJobView> listByPage(String jobName, String jobGroup, String status) {
        Page<?> page = PageUtil.getPage();
        Page<JobView> jobViewPage = jobService.selectJobPage(page, jobName, jobGroup, status);
        return PageHelper.convertTo(jobViewPage, BOJobService::buildBOJobView);
    }

    private static BOJobView buildBOJobView(JobView job) {
        if (Objects.isNull(job)) {
            return null;
        }
        BOJobView jobView = new BOJobView();
        jobView.setJobId(job.getJobId());
        jobView.setJobName(job.getJobName());
        jobView.setJobGroup(job.getJobGroup());
        jobView.setInvokeTarget(job.getInvokeTarget());
        jobView.setCronExpression(job.getCronExpression());
        jobView.setMisfirePolicy(job.getMisfirePolicy());
        jobView.setConcurrent(job.getConcurrent());
        jobView.setStatus(job.getStatus());
        jobView.setCreateBy(job.getCreateBy());
        jobView.setCreateTime(job.getCreateTime());
        jobView.setUpdateBy(job.getUpdateBy());
        jobView.setUpdateTime(job.getUpdateTime());
        jobView.setRemark(job.getRemark());
        jobView.setNextValidTime(job.getNextValidTime());
        return jobView;
    }

    public BOJobView get(String id) {
        JobView jobView = jobService.selectJobById(id);
        return buildBOJobView(jobView);
    }

    public void create(String userId, BOCreateJobRequest request) throws SchedulerException, TaskException {
        CreateJobRequest createJobRequest = new CreateJobRequest();
        createJobRequest.setJobName(request.getJobName());
        createJobRequest.setJobGroup(request.getJobGroup());
        createJobRequest.setInvokeTarget(request.getInvokeTarget());
        createJobRequest.setCronExpression(request.getCronExpression());
        createJobRequest.setMisfirePolicy(request.getMisfirePolicy());
        createJobRequest.setConcurrent(request.getConcurrent());
        createJobRequest.setStatus(request.getStatus());
        createJobRequest.setRemark(request.getRemark());
        createJobRequest.setRequestBy(userId);
        jobService.addJob(createJobRequest);
    }

    public void update(String userId, BOUpdateJobRequest request) throws SchedulerException, TaskException {
        UpdateJobRequest updateJobRequest = new UpdateJobRequest();
        updateJobRequest.setJobId(request.getJobId());
        updateJobRequest.setJobName(request.getJobName());
        updateJobRequest.setJobGroup(request.getJobGroup());
        updateJobRequest.setInvokeTarget(request.getInvokeTarget());
        updateJobRequest.setCronExpression(request.getCronExpression());
        updateJobRequest.setMisfirePolicy(request.getMisfirePolicy());
        updateJobRequest.setConcurrent(request.getConcurrent());
        updateJobRequest.setStatus(request.getStatus());
        updateJobRequest.setRemark(request.getRemark());
        updateJobRequest.setRequestBy(userId);
        jobService.updateJob(updateJobRequest);
    }

    public void delete(String userId, Set<String> ids) {
        jobService.deleteJob(ids);
        log.info("userId={}, deleted Job ids=[{}]", userId, String.join(",", ids));
    }

    public void run(String userId, String jobId) {
        jobService.runJob(jobId);
        log.info("Run job success, userId={}, jobId=[{}]", userId, jobId);
    }

    public void changeStatus(String userId, BOChangeJobStatusRequest request) throws SchedulerException {
        ChangeJobStatusRequest changeJobStatusRequest = new ChangeJobStatusRequest();
        changeJobStatusRequest.setId(request.getJobId());
        changeJobStatusRequest.setStatus(request.getStatus());
        changeJobStatusRequest.setRequestBy(userId);
        jobService.changeStatus(changeJobStatusRequest);
        log.info("Change job success, userId={}, jobId={}, status={}", userId, request.getJobId(), request.getStatus());
    }
}
