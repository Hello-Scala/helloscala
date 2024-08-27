package com.helloscala.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.service.entity.JobLog;
import com.helloscala.service.mapper.JobLogMapper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.job.service.JobLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobLogServiceImpl extends ServiceImpl<JobLogMapper, JobLog> implements JobLogService {
    @Override
    public Page<JobLog> selectJobLogPage(String jobName, String jobGroup, String status, String startTime,
                                         String endTime, Long jobId) {
        LambdaQueryWrapper<JobLog> queryWrapper = new LambdaQueryWrapper<JobLog>()
                .eq(jobId != null, JobLog::getJobId, jobId)
                .like(StringUtils.isNotBlank(jobName), JobLog::getJobName, jobName)
                .like(StringUtils.isNotBlank(jobGroup), JobLog::getJobGroup, jobGroup)
                .eq(StringUtils.isNotBlank(status), JobLog::getStatus, status)
                .between(StringUtils.isNotBlank(startTime), JobLog::getStartTime, startTime, endTime)
                .orderByDesc(JobLog::getCreateTime);

        Page<JobLog> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobLog(List<Long> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cleanJobLog() {
        baseMapper.clean();
    }
}
