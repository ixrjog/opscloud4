package com.baiyi.opscloud.schedule.quartz.service;

import com.baiyi.opscloud.schedule.quartz.ScheduleJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author 修远
 * @Date 2022/1/19 10:53 AM
 * @Since 1.0
 */
@Slf4j
@Component
public class QuartzService {

    @Resource
    private Scheduler scheduler;

//    @PostConstruct
//    public void startScheduler() {
//        try {
//            scheduler.start();
//            log.info("Quartz Scheduler 启动成功!");
//        } catch (SchedulerException e) {
//            log.error("Quartz Scheduler 启动失败: e = {}", e.getMessage());
//        }
//    }

    /**
     * 增加一个job
     *
     * @param jobClass 任务实现类
     * @param jobName  任务名称(建议唯一)z
     * @param group    任务组名
     * @param jobTime  时间表达式 （如：0/5 * * * * ? ）
     * @param jobData  参数
     */
    public void addJob(Class<? extends QuartzJobBean> jobClass, String group, String jobName, String jobTime, Map jobData) {
        // 创建jobDetail实例，绑定Job实现类
        // 指明job的名称，所在组的名称，以及绑定job类
        // 任务名称和组构成任务key
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, group)
                .build();
        // 设置job参数
        if (!CollectionUtils.isEmpty(jobData)) {
            jobDetail.getJobDataMap().putAll(jobData);
        }
        // 定义调度触发规则
        // 使用cornTrigger规则
        // 触发器key
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName, group)
                .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                .withSchedule(CronScheduleBuilder.cronSchedule(jobTime))
                .startNow()
                .build();
        try {
            // 把作业和触发器注册到任务调度中
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            log.error("注册作业错误: e = {}", e.getMessage());
        }
    }

    /**
     * 修改 一个job的 时间表达式
     *
     * @param jobName
     * @param group
     * @param jobTime
     */
    public void updateJob(String jobName, String group, String jobTime) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, group);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobTime)).build();
            // 重启触发器
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param jobName
     * @param group
     */
    public void deleteJob(String group, String jobName) {
        TriggerKey triggerKey = TriggerKey.triggerKey(
                jobName, group);
        JobKey jobKey = JobKey.jobKey(jobName, group);
        try {
            Trigger trigger = scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);
            // 删除任务
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停一个job
     *
     * @param group
     * @param jobName
     */
    public void pauseJob(String group, String jobName) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, group);
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 恢复一个job
     *
     * @param jobName
     * @param group
     */
    public void resumeJob(String jobName, String group) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, group);
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 立即执行一个job
     *
     * @param jobName
     * @param group
     */
    public void runJobNow(String jobName, String group) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, group);
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有计划中的任务列表
     *
     * @return
     * @throws SchedulerException
     */
    public List<ScheduleJob> getAllJob() throws SchedulerException {
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<ScheduleJob> jobList = new ArrayList<ScheduleJob>();
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                ScheduleJob job = ScheduleJob.builder()
                        .name(jobKey.getName())
                        .group(jobKey.getGroup())
                        .description("触发器: " + trigger.getKey())
                        .status(triggerState.name())
                        .build();
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    job.setCronExpression(cronTrigger.getCronExpression());
                }
                jobList.add(job);
            }
        }
        return jobList;
    }

    /**
     * 所有正在运行的job
     *
     * @return
     * @throws SchedulerException
     */
    public List<ScheduleJob> getRunningJob() throws SchedulerException {
        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
        List<ScheduleJob> jobList = new ArrayList<ScheduleJob>(executingJobs.size());
        for (JobExecutionContext executingJob : executingJobs) {
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            ScheduleJob job = ScheduleJob.builder()
                    .name(jobKey.getName())
                    .group(jobKey.getGroup())
                    .description("触发器: " + trigger.getKey())
                    .status(triggerState.name())
                    .build();
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                job.setCronExpression(cronTrigger.getCronExpression());
            }
            jobList.add(job);
        }
        return jobList;
    }
}
