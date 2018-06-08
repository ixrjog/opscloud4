package com.sdg.cmdb.scheduler.task;

import com.sdg.cmdb.domain.esxi.EsxiHostDO;
import com.sdg.cmdb.service.CacheEsxiHostService;
import com.sdg.cmdb.service.VmService;
import com.sdg.cmdb.util.schedule.BaseJob;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;

import java.util.List;

/**
 * Created by liangjian on 2017/8/8.
 */
public class EsxiHostCacheTask implements BaseJob, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(EsxiHostCacheTask.class);


    @Value("#{cmdb['task.open']}")
    private String taskOpen;

    @Resource
    private VmService vmService;


    /**
     * 执行任务
     */
    private static final String taskCorn = "0 0 0 * * ?";

    @Resource
    private CacheEsxiHostService cacheEsxiHostService;

    @Resource
    private SchedulerManager schedulerManager;

    /**
     * 数据同步
     */
    @Override
    public void execute() {
        if (taskOpen.equalsIgnoreCase("true")) {
            String isRunning = cacheEsxiHostService.getKeyByString();
            if (isRunning != null && isRunning.equals("true")) {
                logger.info("EsxiHostCacheTask : task is running");
                return ;
            }
            cacheEsxiHostService.set( "true");
            logger.info("EsxiHostCacheTask : task start");
            List<EsxiHostDO> esxiHosts = vmService.getEsxiHost();
            for(EsxiHostDO esxiHostDO:esxiHosts)
                cacheEsxiHostService.insert(esxiHostDO);
            cacheEsxiHostService.set( "false");
            logger.info("EsxiHostCacheTask : end task");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        schedulerManager.registerJob(this, taskCorn, this.getClass().getSimpleName());
    }

}
