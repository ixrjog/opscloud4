package com.sdg.cmdb.scheduler.task;

import com.sdg.cmdb.util.schedule.BaseJob;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import com.sdg.cmdb.zabbix.ZabbixService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class ZabbixUpdateTomcatVersionTask implements BaseJob, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ZabbixUpdateTomcatVersionTask.class);

    @Value("#{cmdb['task.open']}")
    private String taskOpen;

    /**
     * 执行任务
     */
    private static final String taskCorn = "0 0 0 * * ?";

    @Resource
    private ZabbixService zabbixService;

    @Resource
    private SchedulerManager schedulerManager;

    /**
     * 数据同步
     */
    @Override
    public void execute() {
        if (taskOpen.equalsIgnoreCase("true")) {
            logger.info("ZabbixUpdateTomcatVersionTask : task start");
            zabbixService.refresh();
            logger.info("ZabbixUpdateTomcatVersionTask : task end");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        schedulerManager.registerJob(this, taskCorn, this.getClass().getSimpleName());
    }
}
