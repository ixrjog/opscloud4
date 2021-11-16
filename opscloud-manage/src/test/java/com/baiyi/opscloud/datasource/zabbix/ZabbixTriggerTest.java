package com.baiyi.opscloud.datasource.zabbix;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.datasource.zabbix.base.BaseZabbixTest;
import com.baiyi.opscloud.zabbix.entry.ZabbixTrigger;
import com.baiyi.opscloud.zabbix.datasource.ZabbixTriggerDatasource;
import com.baiyi.opscloud.zabbix.param.base.SeverityType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/8/25 6:35 下午
 * @Version 1.0
 */
@Slf4j
public class ZabbixTriggerTest extends BaseZabbixTest {

    @Resource
    private ZabbixTriggerDatasource zabbixTriggerHandler;

    @Test
    void getTriggersBySeverityTypeTest() {
        List<ZabbixTrigger> zabbixTriggers = zabbixTriggerHandler.getBySeverityType(getConfig().getZabbix(), SeverityType.AVERAGE);
        System.out.println("severityType = " + SeverityType.AVERAGE);
        System.out.println("size = " + zabbixTriggers.size());
        for (ZabbixTrigger zabbixTrigger : zabbixTriggers) {
            System.err.println(JSON.toJSONString(zabbixTrigger));
        }
    }

    @Test
    void getTriggerTest() {
        ZabbixTrigger zabbixTrigger = zabbixTriggerHandler.getById(getConfig().getZabbix(), "44750");
        System.err.println(JSON.toJSONString(zabbixTrigger));
    }
}
