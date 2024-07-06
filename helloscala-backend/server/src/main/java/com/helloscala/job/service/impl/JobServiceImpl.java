package com.helloscala.job.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.entity.Job;
import com.helloscala.common.entity.User;
import com.helloscala.common.enums.ScheduleConstants;
import com.helloscala.common.enums.TaskException;
import com.helloscala.common.mapper.JobMapper;
import com.helloscala.common.mapper.UserMapper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.web.exception.GenericException;
import com.helloscala.job.service.JobService;
import com.helloscala.job.utils.CronUtil;
import com.helloscala.job.utils.ScheduleUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

    private final Scheduler scheduler;

    private final UserMapper userMapper;

    @PostConstruct
    public void init() throws SchedulerException, TaskException {
        scheduler.clear();
        List<Job> jobList = baseMapper.selectList(null);
        for (Job job : jobList) {
            ScheduleUtil.createScheduleJob(scheduler, job);
        }
    }

    @Override
    public Page<Job> selectJobPage(String jobName, String jobGroup, String status) {
        LambdaQueryWrapper<Job> queryWrapper = new LambdaQueryWrapper<Job>()
                .like(StringUtils.isNotBlank(jobName), Job::getJobName,jobName)
                .eq(StringUtils.isNotBlank(jobGroup), Job::getJobGroup,jobGroup)
                .eq(StringUtils.isNotBlank(status), Job::getStatus,status);

        Page<Job> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Job selectJobById(Long jobId) {
        Job job = baseMapper.selectById(jobId);
        Date nextExecution = CronUtil.getNextExecution(job.getCronExpression());
        job.setNextValidTime(nextExecution);
        return job;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addJob(Job job) throws SchedulerException, TaskException {
        checkCronIsValid(job);

        User user = userMapper.selectById(StpUtil.getLoginIdAsString());
        job.setCreateBy(user.getUsername());
        int row = baseMapper.insert(job);
        if (row > 0) ScheduleUtil.createScheduleJob(scheduler, job);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJob(Job job) throws SchedulerException, TaskException {
        checkCronIsValid(job);

        User user = userMapper.selectById(StpUtil.getLoginIdAsString());
        job.setUpdateBy(user.getUsername());
        Job properties = baseMapper.selectById(job.getJobId());
        int row = baseMapper.updateById(job);
        if (row > 0) updateSchedulerJob(job, properties.getJobGroup());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJob(List<Long> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pauseJob(Job job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = baseMapper.updateById(job);
        if (rows > 0) {
            scheduler.pauseJob(ScheduleUtil.getJobKey(jobId, jobGroup));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void runJob(Job job) {
        try {
            Long jobId = job.getJobId();
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
    public void changeStatus(Job job) throws SchedulerException {
        String status = job.getStatus();
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        int row = baseMapper.updateById(job);
        if (row > 0){
            if (ScheduleConstants.Status.NORMAL.getValue().equals(status)) {
                scheduler.resumeJob(ScheduleUtil.getJobKey(jobId, jobGroup));
            } else if (ScheduleConstants.Status.PAUSE.getValue().equals(status)) {
                scheduler.pauseJob(ScheduleUtil.getJobKey(jobId, jobGroup));
            }
        }
    }


    private void checkCronIsValid(Job job) {
        boolean valid = CronUtil.isValid(job.getCronExpression());
        Assert.isTrue(valid,"Invalid Cron expression!");
    }

    public void updateSchedulerJob(Job job, String jobGroup) throws SchedulerException, TaskException {
        Long jobId = job.getJobId();
        JobKey jobKey = ScheduleUtil.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtil.createScheduleJob(scheduler, job);
    }
}
