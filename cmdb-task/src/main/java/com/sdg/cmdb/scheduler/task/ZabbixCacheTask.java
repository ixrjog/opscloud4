package com.sdg.cmdb.scheduler.task;

import com.sdg.cmdb.service.ServerPerfService;

import com.sdg.cmdb.service.impl.ServerPerfServiceImpl;
import com.sdg.cmdb.util.TimeUtils;
import com.sdg.cmdb.util.schedule.BaseJob;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import com.sdg.cmdb.plugin.cache.CacheZabbixService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * Created by liangjian on 2017/3/1.
 */
@Service
public class ZabbixCacheTask implements BaseJob, InitializingBean {


    private static final Logger logger = LoggerFactory.getLogger(ZabbixCacheTask.class);


    @Value("#{cmdb['task.open']}")
    private String taskOpen;

    @Resource
    private ServerPerfService serverPerfService;

    /**
     * 执行任务
     */
    private static final String taskCorn = "0 0 * * * ?";

    @Resource
    private CacheZabbixService cacheZabbixService;

    @Resource
    private SchedulerManager schedulerManager;

    /**
     * 数据同步
     */
    @Override
    public void execute() {
        if (taskOpen.equalsIgnoreCase("true")) {
            if (checkTaskRunning()) {
                logger.info("ZabbixCacheTask : task is running");
                return;
            }
            cacheZabbixService.set(ServerPerfServiceImpl.zabbix_cacha_status, "true");
            SimpleDateFormat sdf = new SimpleDateFormat(TimeUtils.timeFormat);//小写的mm表示的是分钟
            Date date = new Date();
            String gmtModify = sdf.format(date);
            cacheZabbixService.set(ServerPerfServiceImpl.zabbix_cacha_gmtModify, gmtModify);
            logger.info("ZabbixCacheTask : task start");

            serverPerfService.cache();
            cacheZabbixService.set(ServerPerfServiceImpl.zabbix_cacha_status, "false");
            logger.info("ZabbixCacheTask : end task");
        }
    }

    /**
     * 判断任务是否正在运行
     *
     * @return
     */
    private boolean checkTaskRunning() {
        String isRunning = cacheZabbixService.getKeyByString(ServerPerfServiceImpl.zabbix_cacha_status);
        if (isRunning == null ) {
            logger.info("ZabbixCacheTask : status is null");
            return false;
        }
        if(isRunning.equals("false")){
            logger.info("ZabbixCacheTask : status is false");
            return false;
        }
        String gmtModify = cacheZabbixService.getKeyByString(ServerPerfServiceImpl.zabbix_cacha_gmtModify);
        if (gmtModify == null) {
            logger.info("ZabbixCacheTask : gmtModify = null");
            return false;
        }else{
            logger.info("ZabbixCacheTask : gmtModify = {}",gmtModify);
        }
        try {
            return TimeUtils.timeLapse(gmtModify, TimeUtils.hourTime);
        } catch (ParseException pe) {
            return false;
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        schedulerManager.registerJob(this, taskCorn, this.getClass().getSimpleName());
    }
}
