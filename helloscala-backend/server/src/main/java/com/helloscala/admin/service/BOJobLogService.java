package com.helloscala.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.request.BOSearchJobLogRequest;
import com.helloscala.admin.controller.view.BOJobLogView;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.service.service.JobLogService;
import com.helloscala.service.web.request.SearchJobLogRequest;
import com.helloscala.service.web.view.JobLogView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BOJobLogService {
    private final JobLogService jobLogService;

    public Page<BOJobLogView> search(BOSearchJobLogRequest request) {
        SearchJobLogRequest searchJobLogRequest = new SearchJobLogRequest();
        searchJobLogRequest.setId(request.getJobId());
        searchJobLogRequest.setJobName(request.getJobName());
        searchJobLogRequest.setJobGroup(request.getJobGroup());
        searchJobLogRequest.setStatus(request.getStatus());
        searchJobLogRequest.setStartTime(request.getStartTime());
        searchJobLogRequest.setEndTime(request.getEndTime());
        Page<JobLogView> jobLogViewPage = jobLogService.selectJobLogPage(searchJobLogRequest);
        return PageHelper.convertTo(jobLogViewPage, jobLog -> {
            BOJobLogView jobLogView = new BOJobLogView();
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

    public void bulkDelete(String userId, Set<String> ids) {
        jobLogService.deleteJobLog(ids);
        log.info("userId={}, deleted JobLogs ids=[{}]", userId, String.join(",", ids));
    }

    public void clean(String userId) {
        jobLogService.cleanJobLog();
        log.info("userId={}, cleaned JobLogs", userId);
    }
}
