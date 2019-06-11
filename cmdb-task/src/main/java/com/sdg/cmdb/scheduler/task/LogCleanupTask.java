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

@Component
@Slf4j
public class LogcleanupTask implements BaseJob {
    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;

    public static final String LOGCLEANUP_TASK_KEY = "LogcleanupTask";

    @Autowired
    private CacheKeyService cacheKeyService;

    @Value("#{cmdb['task.open']}")
    private String taskOpen;

    @Autowired
    private LogcleanupService logcleanupService;

    @Override
    @Scheduled(cron = "0 0 */2 * * ?")
    public void execute() {
        if (!StringUtils.isEmpty(taskOpen) && taskOpen.equalsIgnoreCase("true")) {
            if (cacheKeyService.checkRunning(LOGCLEANUP_TASK_KEY, 60))
                return;
            logcleanupService.task();
            // 任务结束插入 结束
            cacheKeyService.set(LOGCLEANUP_TASK_KEY, "false", 2);
        }
    }
}
