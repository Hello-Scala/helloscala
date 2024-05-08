package com.helloscala.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.JobLog;

import java.util.List;


public interface JobLogService extends IService<JobLog> {

    /**
     * 分页
     * @param jobName
     * @param jobGroup
     * @param status
     * @param startTime
     * @param endTime
     * @param jobId
     * @return
     */
    ResponseResult selectJobLogPage(String jobName, String jobGroup, String status, String startTime,
                            String endTime, Long jobId);

    /**
     * 删除日志
     * @param ids
     * @return
     */
    ResponseResult deleteJobLog(List<Long> ids);

    /**
     * 清空日志
     * @return
     */
    ResponseResult cleanJobLog();
}
