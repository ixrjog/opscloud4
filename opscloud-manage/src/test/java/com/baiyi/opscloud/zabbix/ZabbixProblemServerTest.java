package com.baiyi.opscloud.zabbix;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.alarm.AlarmCenter;
import com.baiyi.opscloud.zabbix.base.SeverityType;
import com.baiyi.opscloud.zabbix.entry.ZabbixTrigger;
import com.baiyi.opscloud.zabbix.server.ZabbixProblemServer;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/2/18 10:38 上午
 * @Version 1.0
 */
public class ZabbixProblemServerTest extends BaseUnit {

    @Resource
    private ZabbixProblemServer zabbixProblemServer;


    @Resource
    private AlarmCenter alarmCenter;

    @Test
    void zabbixLoginTest() {
        List<ZabbixTrigger> list = zabbixProblemServer.getTriggers(SeverityType.WARNING);
        list.forEach(e ->
                System.err.println(JSON.toJSON(e))
        );
    }

    @Test
    void cacheTriggersTest() {
        zabbixProblemServer.cacheTriggers(zabbixProblemServer.getTriggers(SeverityType.WARNING));
    }


    @Test
    void getTriggerMapTest() {
        zabbixProblemServer.cacheTriggers(zabbixProblemServer.getTriggers(SeverityType.WARNING));
        Map<String, List<ZabbixTrigger>> triggerMap = zabbixProblemServer.getTriggerMapForCache();
        System.err.println(JSON.toJSON(triggerMap));

        alarmCenter.notice(triggerMap);

    }
}