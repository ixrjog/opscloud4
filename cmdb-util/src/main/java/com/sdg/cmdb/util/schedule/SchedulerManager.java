package com.sdg.cmdb.util.schedule;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zxxiao on 15/6/26.
 */
@Service("schedulerManager")
public class SchedulerManager implements DisposableBean {

    private static Logger logger = LoggerFactory.getLogger(SchedulerManager.class);

    @Resource
    private Scheduler cloudstackScheduler;

    private ExecutorService executors = Executors.newFixedThreadPool(10);

    public void registerJob(BaseJob baseJob, String cron, String name) {
        name += "-job";
        TriggerKey triggerKey = TriggerKey.triggerKey(name);
        try {
            CronTrigger trigger = (CronTrigger) cloudstackScheduler.getTrigger(triggerKey);
            if (trigger == null) {
                JobDetail jobDetail = JobBuilder.newJob(SchedulerJob.class)
                        .withIdentity(name)
                        .build();

                jobDetail.getJobDataMap().put("job", baseJob);

                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(name)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
                cloudstackScheduler.scheduleJob(jobDetail, trigger);
                logger.info("scheduler job: " + name);
            } else {
                trigger = trigger.getTriggerBuilder()
                        .withIdentity(triggerKey)
                        .withSchedule(
                                CronScheduleBuilder.cronSchedule(cron))
                        .build();
                cloudstackScheduler.rescheduleJob(triggerKey, trigger);
                logger.info("rescheduler job: " + name);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 一次性执行的任务，非定时任务
     * @param baseJob
     */
    public void registerJob(final BaseJob baseJob) {
        executors.execute(() -> baseJob.execute());
    }

    /**
     * 移除指定任务
     * @param name
     */
    public void unregisterJob(String name) {
        name += "-job";
        JobKey jobKey = JobKey.jobKey(name);
        TriggerKey triggerKey = TriggerKey.triggerKey(name);

        if (triggerKey == null || jobKey == null) {
            return;
        }

        try {
            cloudstackScheduler.pauseTrigger(triggerKey);
            cloudstackScheduler.unscheduleJob(triggerKey);
            cloudstackScheduler.deleteJob(jobKey);
            logger.info("remove job: " + name);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() throws Exception {
        cloudstackScheduler.shutdown();
        executors.shutdown();
    }
}
