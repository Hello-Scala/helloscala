package com.helloscala.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.utils.CronUtil;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.web.exception.GenericException;
import com.helloscala.common.web.exception.NotFoundException;
import com.helloscala.common.web.exception.TaskException;
import com.helloscala.service.entity.Job;
import com.helloscala.service.enums.ScheduleConstants;
import com.helloscala.service.mapper.JobMapper;
import com.helloscala.service.service.JobService;
import com.helloscala.service.service.util.ScheduleUtil;
import com.helloscala.service.web.request.ChangeJobStatusRequest;
import com.helloscala.service.web.request.CreateJobRequest;
import com.helloscala.service.web.request.UpdateJobRequest;
import com.helloscala.service.web.view.JobView;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {
    private final Scheduler scheduler;

    @PostConstruct
    public void init() throws SchedulerException, TaskException {
        scheduler.clear();
        List<Job> jobList = baseMapper.selectList(null);
        for (Job job : jobList) {
            ScheduleUtil.createScheduleJob(scheduler, job);
        }
    }

    @Override
    public Page<JobView> selectJobPage(Page<?> page, String jobName, String jobGroup, String status) {
        LambdaQueryWrapper<Job> queryWrapper = new LambdaQueryWrapper<Job>()
                .like(StringUtils.isNotBlank(jobName), Job::getJobName, jobName)
                .eq(StringUtils.isNotBlank(jobGroup), Job::getJobGroup, jobGroup)
                .eq(StringUtils.isNotBlank(status), Job::getStatus, status);

        Page<Job> jobPage = baseMapper.selectPage(PageHelper.of(page), queryWrapper);
        return PageHelper.convertTo(jobPage, JobServiceImpl::buildJobView);
    }

    private static JobView buildJobView(Job job) {
        if (Objects.isNull(job)) {
            return null;
        }
        Date nextExecution = CronUtil.getNextExecution(job.getCronExpression());
        JobView jobView = new JobView();
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
        jobView.setNextValidTime(nextExecution);
        return jobView;
    }

    @Override
    public JobView selectJobById(String jobId) {
        Job job = baseMapper.selectById(jobId);
        return buildJobView(job);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addJob(CreateJobRequest request) throws SchedulerException, TaskException {
        checkCronIsValid(request.getCronExpression());
        Job job = new Job();
        job.setJobName(request.getJobName());
        job.setJobGroup(request.getJobGroup());
        job.setInvokeTarget(request.getInvokeTarget());
        job.setCronExpression(request.getCronExpression());
        job.setMisfirePolicy(request.getMisfirePolicy());
        job.setConcurrent(request.getConcurrent());
        job.setStatus(request.getStatus());
        job.setCreateBy(request.getRequestBy());
        job.setCreateTime(new Date());
        job.setRemark(request.getRemark());
        int row = baseMapper.insert(job);
        if (row > 0) ScheduleUtil.createScheduleJob(scheduler, job);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJob(UpdateJobRequest request) throws SchedulerException, TaskException {
        Job job = getOrThrow(request.getJobId());
        checkCronIsValid(request.getCronExpression());

        job.setJobName(request.getJobName());
        job.setJobGroup(request.getJobGroup());
        job.setInvokeTarget(request.getInvokeTarget());
        job.setCronExpression(request.getCronExpression());
        job.setMisfirePolicy(request.getMisfirePolicy());
        job.setConcurrent(request.getConcurrent());
        job.setStatus(request.getStatus());
        job.setUpdateBy(request.getRequestBy());
        job.setUpdateTime(new Date());
        job.setRemark(request.getRemark());
        int row = baseMapper.updateById(job);
        if (row > 0) updateSchedulerJob(job, job.getJobGroup());
    }

    private @NotNull Job getOrThrow(String jobId) {
        Job job = baseMapper.selectById(jobId);
        if (Objects.isNull(job)) {
            throw new NotFoundException("Job not found, jobId={}!", jobId);
        }
        return job;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJob(Set<String> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pauseJob(String id) throws SchedulerException {
        Job job = getOrThrow(id);
        String jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = baseMapper.updateById(job);
        if (rows > 0) {
            scheduler.pauseJob(ScheduleUtil.getJobKey(jobId, jobGroup));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void runJob(String id) {
        Job job = getOrThrow(id);
        try {
            String jobId = job.getJobId();
            String jobGroup = job.getJobGroup();
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(ScheduleConstants.TASK_PROPERTIES, job);
            scheduler.triggerJob(ScheduleUtil.getJobKey(jobId, jobGroup), dataMap);
        } catch (Exception e) {
            throw new GenericException("Job run failed, msg={}!" + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(ChangeJobStatusRequest request) throws SchedulerException {
        Job job = getOrThrow(request.getId());
        String jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(request.getStatus());
        job.setUpdateTime(new Date());
        job.setUpdateBy(request.getRequestBy());
        int row = baseMapper.updateById(job);
        if (row > 0) {
            if (ScheduleConstants.Status.NORMAL.getValue().equals(request.getStatus())) {
                scheduler.resumeJob(ScheduleUtil.getJobKey(jobId, jobGroup));
            } else if (ScheduleConstants.Status.PAUSE.getValue().equals(request.getStatus())) {
                scheduler.pauseJob(ScheduleUtil.getJobKey(jobId, jobGroup));
            }
        }
    }

    private void checkCronIsValid(String cronExpression) {
        boolean valid = CronUtil.isValid(cronExpression);
        Assert.isTrue(valid, "Invalid Cron expression!");
    }

    public void updateSchedulerJob(Job job, String jobGroup) throws SchedulerException, TaskException {
        String jobId = job.getJobId();
        JobKey jobKey = ScheduleUtil.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtil.createScheduleJob(scheduler, job);
    }
}
