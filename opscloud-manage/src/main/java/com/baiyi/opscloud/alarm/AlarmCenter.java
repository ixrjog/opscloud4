package com.baiyi.opscloud.alarm;

import com.baiyi.opscloud.zabbix.entry.ZabbixTrigger;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/2/18 4:59 下午
 * @Version 1.0
 */

public interface AlarmCenter {

    void notice(Map<String, List<ZabbixTrigger>> triggerMap);
}
