package com.baiyi.opscloud.scheduler;

import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.CronUtil;
import com.baiyi.opscloud.domain.vo.datasource.ScheduleVO;
import com.google.common.collect.Lists;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
@RequiredArgsConstructor
public class QuartzService {

    private final Scheduler scheduler;

    @PostConstruct
    public void scheduleJob() throws SchedulerException {
        scheduler.start();
    }

    /**
     * 增加一个job
     *
     * @param jobClass 任务实现类
     * @param jobName  任务名称(建议唯一)
     * @param group    任务组名
     * @param jobTime  时间表达式 （如：0/5 * * * * ? ）
     * @param jobData  参数
     */
    public void addJob(Class<? extends QuartzJobBean> jobClass, String group, String jobName, String jobTime, String jobDescription, Map<? extends String, ?> jobData) {
        // 创建jobDetail实例，绑定Job实现类
        // 指明job的名称，所在组的名称，以及绑定job类
        // 任务名称和组构成任务key
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, group)
                .withDescription(jobDescription)
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
            log.error("注册作业错误: {}", e.getMessage());
        }
    }

    /**
     * 修改 一个job的 时间表达式
     *
     * @param jobName
     * @param group
     * @param jobTime
     */
    public void updateJob(String group, String jobName, String jobTime) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, group);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder()
                    .withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobTime))
                    .build();
            // 重启触发器
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            log.error("更新作业错误: {}", e.getMessage());
        }
    }

    /**
     * @param jobName
     * @param group   instanceUuid
     */
    public void deleteJob(String group, String jobName) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, group);
            Trigger trigger = scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);
            // 删除任务
            scheduler.deleteJob(JobKey.jobKey(jobName, group));
        } catch (SchedulerException e) {
            log.error("删除作业错误: {}", e.getMessage());
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
            log.error("暂停作业错误: {}", e.getMessage());
        }
    }

    /**
     * 恢复一个job
     *
     * @param jobName
     * @param group
     */
    public void resumeJob(String group, String jobName) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, group);
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            log.error("恢复作业错误: {}", e.getMessage());
        }
    }

    /**
     * 立即执行一个job
     *
     * @param jobName
     * @param group
     */
    public void runJobNow(String group, String jobName) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, group);
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            log.error("执行作业错误: {}", e.getMessage());
        }
    }

    /**
     * 查询数据源实例任务数量
     *
     * @param group
     * @return
     */
    public int queryInstanceJobSize(String group) {
        try {
            GroupMatcher<JobKey> matcher = GroupMatcher.groupEquals(group);
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            return jobKeys.size();
        } catch (SchedulerException e) {
            log.error("查询数据源实例任务数量: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * 指定条件查询计划中的任务列表
     *
     * @return
     * @throws SchedulerException
     */
    public List<ScheduleVO.Job> queryJob(String group) throws SchedulerException {
        GroupMatcher<JobKey> matcher = GroupMatcher.groupEquals(group);
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<ScheduleVO.Job> jobs = Lists.newArrayList();
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            jobs.addAll(triggers.stream().map(e -> buildJob(jobKey, e)).toList());
        }
        return jobs;
    }

    private ScheduleVO.Job buildJob(JobKey jobKey, Trigger trigger) {
        try {
            final Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            final JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            ScheduleVO.Job job = ScheduleVO.Job.builder()
                    .name(jobKey.getName())
                    .group(jobKey.getGroup())
                    .description(jobDetail.getDescription())
                    .status(triggerState.name())
                    .build();
            if (trigger instanceof CronTrigger cronTrigger) {
                job.setCronExpression(cronTrigger.getCronExpression());
                job.setExecutionTime(CronUtil.recentTime(cronTrigger.getCronExpression(), 5));
            }
            return job;
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            throw new OCException(e.getMessage());
        }
    }

    /**
     * 获取所有计划中的任务列表
     *
     * @return
     * @throws SchedulerException
     */
    public List<ScheduleVO.Job> getAllJob() throws SchedulerException {
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<ScheduleVO.Job> jobs = Lists.newArrayList();
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            jobs.addAll(triggers.stream().map(e -> buildJob(jobKey, e)).toList());
        }
        return jobs;
    }

    /**
     * 所有正在运行的job
     *
     * @return
     * @throws SchedulerException
     */
    public List<ScheduleVO.Job> getRunningJob() throws SchedulerException {
        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
        List<ScheduleVO.Job> jobList = new ArrayList<>(executingJobs.size());
        for (JobExecutionContext executingJob : executingJobs) {
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            ScheduleVO.Job job = ScheduleVO.Job.builder()
                    .name(jobKey.getName())
                    .group(jobKey.getGroup())
                    .description("触发器: " + trigger.getKey())
                    .status(triggerState.name())
                    .build();
            if (trigger instanceof CronTrigger cronTrigger) {
                job.setCronExpression(cronTrigger.getCronExpression());
            }
            jobList.add(job);
        }
        return jobList;
    }

    public Boolean checkJobExist(String group, String jobName) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, group);
            return scheduler.getJobDetail(jobKey) != null;
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            return false;
        }
    }

}