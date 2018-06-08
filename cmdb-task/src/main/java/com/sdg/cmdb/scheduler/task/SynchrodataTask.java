package com.sdg.cmdb.scheduler.task;

import com.sdg.cmdb.service.EcsService;
import com.sdg.cmdb.service.KeyBoxService;
import com.sdg.cmdb.service.VmService;
import com.sdg.cmdb.util.schedule.BaseJob;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import com.sdg.cmdb.zabbix.ZabbixService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zxxiao on 2017/2/6.
 */
@Service
public class SynchrodataTask implements BaseJob, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SynchrodataTask.class);

    @Resource
    private ZabbixService zabbixService;

    @Resource
    private VmService vmService;

    @Resource
    private EcsService ecsService;

    @Resource
    private KeyBoxService keyBoxService;

    /**
     * 01:00 执行任务
     */
    private static final String taskCorn = "0 0 1 * * ?";

    @Resource
    private SchedulerManager schedulerManager;

    /**
     * 数据同步
     */
    @Override
    public void execute() {
        logger.info("Synchrodata : Zabbix refresh");
        zabbixService.refresh();

        logger.info("Synchrodata : VCSA refresh");
        vmService.vmRefresh();

        logger.info("Synchrodata : Aliyun ECS refresh");
        ecsService.ecsRefresh();

        logger.info("Synchrodata : Keybox check user");
        keyBoxService.checkUser();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        schedulerManager.registerJob(this, taskCorn, this.getClass().getSimpleName());
    }
}
