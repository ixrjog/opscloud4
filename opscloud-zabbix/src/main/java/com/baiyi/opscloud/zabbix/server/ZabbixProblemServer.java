package com.baiyi.opscloud.zabbix.server;

import com.baiyi.opscloud.zabbix.base.SeverityType;
import com.baiyi.opscloud.zabbix.entry.ZabbixTrigger;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/2/18 10:16 上午
 * @Version 1.0
 */
public interface ZabbixProblemServer {

    List<ZabbixTrigger> getTriggers(SeverityType type);

    void cacheTriggers(List<ZabbixTrigger> triggers);

    Map<String, List<ZabbixTrigger>> getTriggerMapForCache();
}
