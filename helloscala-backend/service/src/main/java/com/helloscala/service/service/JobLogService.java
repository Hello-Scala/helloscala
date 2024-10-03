package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.JobLog;
import com.helloscala.service.web.request.SearchJobLogRequest;
import com.helloscala.service.web.view.JobLogView;

import java.util.Set;


public interface JobLogService extends IService<JobLog> {
    Page<JobLogView> selectJobLogPage(SearchJobLogRequest request);

    void deleteJobLog(Set<String> ids);

    void cleanJobLog();
}
