package com.sdg.cmdb.scheduler.task;

import com.sdg.cmdb.util.schedule.BaseJob;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import com.sdg.cmdb.zabbix.LogCleanupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by liangjian on 2017/4/1.
 * 日志清理计划任务
 */
@Service
public class LogCleanupTask implements BaseJob, InitializingBean {


    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;

    private static final Logger logger = LoggerFactory.getLogger(LogCleanupTask.class);

    /**
     * 执行任务2小时一次
     */
    private static final String taskCorn = "0 0 */2 * * ?";

    @Resource
    private SchedulerManager schedulerManager;

    @Resource
    private LogCleanupService logCleanupService;


    @Override
    public void execute() {
        if (!invokeEnv.equalsIgnoreCase("getway")) return;
        logger.info("LogCleanup : task start");
        logCleanupService.task();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        schedulerManager.registerJob(this, taskCorn, this.getClass().getSimpleName());
    }

}
