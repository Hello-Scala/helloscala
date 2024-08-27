package com.helloscala.service.service.quartz;

import com.helloscala.common.entity.Job;
import com.helloscala.service.service.util.JobInvokeUtil;
import org.quartz.JobExecutionContext;

public class AllowConcurrentJob extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, Job job) throws Exception {
        JobInvokeUtil.invokeMethod(job);
    }
}
