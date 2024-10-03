package com.helloscala.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.job.service.JobLogService;
import com.helloscala.service.entity.JobLog;
import com.helloscala.service.mapper.JobLogMapper;
import com.helloscala.service.web.request.SearchJobLogRequest;
import com.helloscala.service.web.view.JobLogView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class JobLogServiceImpl extends ServiceImpl<JobLogMapper, JobLog> implements JobLogService {
    @Override
    public Page<JobLogView> selectJobLogPage(SearchJobLogRequest request) {
        LambdaQueryWrapper<JobLog> queryWrapper = new LambdaQueryWrapper<JobLog>()
                .eq(request.getId() != null, JobLog::getJobId, request.getId())
                .like(StringUtils.isNotBlank(request.getJobName()), JobLog::getJobName, request.getJobName())
                .like(StringUtils.isNotBlank(request.getJobGroup()), JobLog::getJobGroup, request.getJobGroup())
                .eq(StringUtils.isNotBlank(request.getStatus()), JobLog::getStatus, request.getStatus())
                .between(StringUtils.isNotBlank(request.getStartTime()), JobLog::getStartTime, request.getStartTime(), request.getEndTime())
                .orderByDesc(JobLog::getCreateTime);

        Page<JobLog> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        Page<JobLog> jobLogPage = baseMapper.selectPage(page, queryWrapper);
        return PageHelper.convertTo(jobLogPage, jobLog -> {
            JobLogView jobLogView = new JobLogView();
            jobLogView.setId(jobLog.getId());
            jobLogView.setJobId(jobLog.getJobId());
            jobLogView.setJobName(jobLog.getJobName());
            jobLogView.setJobGroup(jobLog.getJobGroup());
            jobLogView.setInvokeTarget(jobLog.getInvokeTarget());
            jobLogView.setJobMessage(jobLog.getJobMessage());
            jobLogView.setStatus(jobLog.getStatus());
            jobLogView.setExceptionInfo(jobLog.getExceptionInfo());
            jobLogView.setCreateTime(jobLog.getCreateTime());
            jobLogView.setStartTime(jobLog.getStartTime());
            jobLogView.setStopTime(jobLog.getStopTime());
            return jobLogView;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobLog(Set<String> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cleanJobLog() {
        baseMapper.clean();
    }
}
