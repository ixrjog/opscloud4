package com.sdg.cmdb.scheduler.task;


import com.sdg.cmdb.service.CacheKeyService;
import com.sdg.cmdb.service.LogcleanupService;
import com.sdg.cmdb.util.schedule.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


/**
 * Created by liangjian on 2017/4/1.
 * 日志清理采样定时任务
 */
@Component
@Slf4j
public class LogcleanupUpdateTask implements BaseJob {

    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;

    public static final String LOGCLEANUP_UPDATE_TASK_KEY = "LogcleanupUpdateTask";

    @Resource
    private CacheKeyService cacheKeyService;

    @Value("#{cmdb['task.open']}")
    private String taskOpen;

    @Autowired
    private LogcleanupService logcleanupService;

    @Override
    @Scheduled(cron = "0 0 */1 * * ?")
    public void execute() {
        if (!StringUtils.isEmpty(taskOpen) && taskOpen.equalsIgnoreCase("true")) {
            if (cacheKeyService.checkRunning(LOGCLEANUP_UPDATE_TASK_KEY, 60))
                return;
            logcleanupService.updateLogcleanupServers();
            // 任务结束插入 结束
            cacheKeyService.set(LOGCLEANUP_UPDATE_TASK_KEY, "false", 2);
        }
    }
}
