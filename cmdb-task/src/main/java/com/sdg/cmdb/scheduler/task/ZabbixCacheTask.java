package com.sdg.cmdb.scheduler.task;

import com.sdg.cmdb.plugin.cache.CacheZabbixService;
import com.sdg.cmdb.service.ServerPerfService;
import com.sdg.cmdb.service.impl.ServerPerfServiceImpl;
import com.sdg.cmdb.util.TimeUtils;
import com.sdg.cmdb.util.schedule.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * Created by liangjian on 2017/3/1.
 */
@Component
@Slf4j
public class ZabbixCacheTask implements BaseJob {

    @Value("#{cmdb['task.open']}")
    private String taskOpen;

    @Resource
    private ServerPerfService serverPerfService;

    @Resource
    private CacheZabbixService cacheZabbixService;

    /**
     * 数据同步
     */
    @Override
    @Scheduled(cron = "0 0 * * * ?")
    public void execute() {
        if (taskOpen.equalsIgnoreCase("true")) {
            if (checkTaskRunning()) {
                log.info("ZabbixCacheTask : task is running");
                return;
            }
            cacheZabbixService.set(ServerPerfServiceImpl.zabbix_cacha_status, "true");
            SimpleDateFormat sdf = new SimpleDateFormat(TimeUtils.timeFormat);//小写的mm表示的是分钟
            Date date = new Date();
            String gmtModify = sdf.format(date);
            cacheZabbixService.set(ServerPerfServiceImpl.zabbix_cacha_gmtModify, gmtModify);
            log.info("ZabbixCacheTask : task start");

            serverPerfService.cache();
            cacheZabbixService.set(ServerPerfServiceImpl.zabbix_cacha_status, "false");
            log.info("ZabbixCacheTask : end task");
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
            log.info("ZabbixCacheTask : status is null");
            return false;
        }
        if(isRunning.equals("false")){
            log.info("ZabbixCacheTask : status is false");
            return false;
        }
        String gmtModify = cacheZabbixService.getKeyByString(ServerPerfServiceImpl.zabbix_cacha_gmtModify);
        if (gmtModify == null) {
            log.info("ZabbixCacheTask : gmtModify = null");
            return false;
        }else{
            log.info("ZabbixCacheTask : gmtModify = {}",gmtModify);
        }
        try {
            return TimeUtils.timeLapse(gmtModify, TimeUtils.hourTime);
        } catch (ParseException pe) {
            return false;
        }
    }
}
