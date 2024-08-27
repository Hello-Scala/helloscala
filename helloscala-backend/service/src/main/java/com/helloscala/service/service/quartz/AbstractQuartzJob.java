package com.helloscala.service.service.quartz;

import com.helloscala.service.entity.Job;
import com.helloscala.service.entity.JobLog;
import com.helloscala.service.enums.ScheduleConstants;
import com.helloscala.service.enums.YesOrNoEnum;
import com.helloscala.service.mapper.JobLogMapper;
import com.helloscala.common.utils.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;


public abstract class AbstractQuartzJob implements org.quartz.Job {
    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);

    private static final ThreadLocal<Date> THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) {
        Job job = new Job();
        BeanUtils.copyProperties(context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES), job);
        try {
            before(context, job);
            doExecute(context, job);
            after(context, job, null);
        } catch (Exception e) {
            log.error("Failed to run job, job name={}", job.getJobName(), e);
            after(context, job, e);
        }
    }

    protected void before(JobExecutionContext context, Job job) {
        THREAD_LOCAL.set(new Date());
    }

    protected void after(JobExecutionContext context, Job job, Exception e) {
        Date startTime = THREAD_LOCAL.get();
        THREAD_LOCAL.remove();
        if (!job.getInvokeTarget().contains("redisTimer")) {
            final JobLog jobLog = new JobLog();
            jobLog.setJobId(job.getJobId());
            jobLog.setJobName(job.getJobName());
            jobLog.setJobGroup(job.getJobGroup());
            jobLog.setInvokeTarget(job.getInvokeTarget());
            jobLog.setStartTime(startTime);
            jobLog.setStopTime(new Date());
            long runMs = jobLog.getStopTime().getTime() - jobLog.getStartTime().getTime();
            jobLog.setJobMessage(jobLog.getJobName() + " TimeUsed :" + runMs + " Mills");
            if (e != null) {
                jobLog.setStatus(YesOrNoEnum.YES.getCodeToString());
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw, true));
                String str = sw.toString();
                String errorMsg = StringUtils.substring(str, 0, 2000);
                jobLog.setExceptionInfo(errorMsg);
            } else {
                jobLog.setStatus(YesOrNoEnum.NO.getCode() + "");
            }

            SpringUtil.getBean(JobLogMapper.class).insert(jobLog);
        }
    }

    protected abstract void doExecute(JobExecutionContext context, Job job) throws Exception;
}
