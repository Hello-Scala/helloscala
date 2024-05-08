package com.helloscala.quartz;

import com.helloscala.entity.Job;
import com.helloscala.utils.JobInvokeUtil;
import org.quartz.JobExecutionContext;

public class QuartzJobExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, Job job) throws Exception {
        JobInvokeUtil.invokeMethod(job);
    }
}
