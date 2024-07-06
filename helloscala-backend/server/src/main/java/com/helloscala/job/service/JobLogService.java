package com.helloscala.job.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.entity.JobLog;

import java.util.List;


public interface JobLogService extends IService<JobLog> {
    Page<JobLog> selectJobLogPage(String jobName, String jobGroup, String status, String startTime,
                                  String endTime, Long jobId);

    void deleteJobLog(List<Long> ids);

    void cleanJobLog();
}
