package com.helloscala.job.quartz;

import com.helloscala.common.entity.Job;
import com.helloscala.job.utils.JobInvokeUtil;
import org.quartz.JobExecutionContext;

public class QuartzJobExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, Job job) throws Exception {
        JobInvokeUtil.invokeMethod(job);
    }
}
