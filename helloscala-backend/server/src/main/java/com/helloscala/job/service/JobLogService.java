package com.helloscala.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.JobLog;

import java.util.List;


public interface JobLogService extends IService<JobLog> {
    ResponseResult selectJobLogPage(String jobName, String jobGroup, String status, String startTime,
                            String endTime, Long jobId);

    ResponseResult deleteJobLog(List<Long> ids);

    ResponseResult cleanJobLog();
}
