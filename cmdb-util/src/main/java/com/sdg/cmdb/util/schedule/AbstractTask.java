package com.sdg.cmdb.util.schedule;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 抽象任务描述，用于在当前上下文下创建合适的调度任务
 * Created by zxxiao on 2017/6/28.
 */
@Service
public class AbstractTask {

    @Resource
    private SchedulerManager schedulerManager;

    /**
     * 注册任意任务
     * @param taskName
     * @param cron
     * @param baseJob
     */
    public void registerTask(String taskName, String cron, BaseJob baseJob) {
        schedulerManager.registerJob(baseJob, cron, taskName);
    }
}
