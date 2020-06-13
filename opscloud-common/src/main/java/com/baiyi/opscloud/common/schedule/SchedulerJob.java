package com.baiyi.opscloud.common.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class SchedulerJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        /**
         * 1.获取任务，并执行
         */
        BaseJob job = (BaseJob) context.getJobDetail().getJobDataMap().get("job");
        job.execute();
    }

}
