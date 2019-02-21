package com.sdg.cmdb.scheduler.task;

import com.sdg.cmdb.service.LogCleanupService;
import com.sdg.cmdb.util.schedule.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by liangjian on 2017/4/1.
 * 日志清理计划任务
 */
@Component
@Slf4j
public class LogCleanupTask implements BaseJob {

    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;

    @Resource
    private LogCleanupService logCleanupService;

    @Override
    @Scheduled(cron = "0 0 */2 * * ?")
    public void execute() {
        if (!invokeEnv.equalsIgnoreCase("getway")) return;
        log.info("LogCleanup : task start");
        logCleanupService.task();
    }
}
