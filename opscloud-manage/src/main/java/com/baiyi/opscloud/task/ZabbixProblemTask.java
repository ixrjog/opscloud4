package com.baiyi.opscloud.task;

import com.baiyi.opscloud.alarm.AlarmCenter;
import com.baiyi.opscloud.zabbix.base.SeverityType;
import com.baiyi.opscloud.zabbix.entry.ZabbixTrigger;
import com.baiyi.opscloud.zabbix.server.ZabbixProblemServer;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/2/20 4:24 下午
 * @Since 1.0
 */
@Slf4j
@Component
public class ZabbixProblemTask {

    @Resource
    private ZabbixProblemServer zabbixProblemServer;

    @Resource
    private AlarmCenter alarmCenter;

    public static final String TASK_ZABBIX_PROBLEM_ALARM_KEY = "TASK_ZABBIX_PROBLEM_ALARM_KEY";

    @Scheduled(cron = "0 */1 * * * ?")
    @SchedulerLock(name = "zabbixProblemAlarmTask", lockAtMostFor = "1m", lockAtLeastFor = "1m")
    public void zabbixProblemAlarmTask() {
        log.info("任务启动: Zabbix问题告警任务！");
        zabbixProblemServer.cacheTriggers(zabbixProblemServer.getTriggers(SeverityType.WARNING));
        Map<String, List<ZabbixTrigger>> triggerMap = zabbixProblemServer.getTriggerMapForCache();
        alarmCenter.notice(triggerMap);
    }

}
