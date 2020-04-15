package com.baiyi.opscloud.common.schedule;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service("schedulerManager")
public class SchedulerManager implements DisposableBean {

    @Resource
    private Scheduler cloudstackScheduler;

    private ExecutorService executors = Executors.newFixedThreadPool(50);

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
                log.info("scheduler job: " + name);
            } else {
                trigger = trigger.getTriggerBuilder()
                        .withIdentity(triggerKey)
                        .withSchedule(
                                CronScheduleBuilder.cronSchedule(cron))
                        .build();
                cloudstackScheduler.rescheduleJob(triggerKey, trigger);
                log.info("rescheduler job: " + name);
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
            log.info("remove job: " + name);
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
