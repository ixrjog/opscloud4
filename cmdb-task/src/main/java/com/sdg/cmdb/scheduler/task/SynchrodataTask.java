package com.sdg.cmdb.scheduler.task;

import com.sdg.cmdb.service.EcsService;
import com.sdg.cmdb.service.KeyBoxService;
import com.sdg.cmdb.service.VmService;
import com.sdg.cmdb.service.ZabbixService;
import com.sdg.cmdb.util.schedule.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by zxxiao on 2017/2/6.
 */
@Component
@Slf4j
public class SynchrodataTask implements BaseJob {

    @Resource
    private ZabbixService zabbixService;

    @Resource
    private EcsService ecsService;

    /**
     * 数据同步
     */
    @Override
    @Scheduled(cron = "0 0 1 * * ?")
    public void execute() {
        log.info("Synchrodata : Zabbix refresh");
        zabbixService.refresh();
        log.info("Synchrodata : Aliyun ECS refresh");
        ecsService.ecsRefresh();
    }
}
