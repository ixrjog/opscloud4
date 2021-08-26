package com.baiyi.opscloud.datasource.zabbix;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.datasource.zabbix.base.BaseZabbixTest;
import com.baiyi.opscloud.zabbix.entry.ZabbixTrigger;
import com.baiyi.opscloud.zabbix.handler.ZabbixTriggerHandler;
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
    private ZabbixTriggerHandler zabbixTriggerHandler;

    @Test
    void getTriggersBySeverityTypeTest() {
        List<ZabbixTrigger> zabbixTriggers = zabbixTriggerHandler.getTriggersBySeverityType(getConfig().getZabbix(), SeverityType.WARNING);
        for (ZabbixTrigger zabbixTrigger : zabbixTriggers) {
            System.err.print(JSON.toJSONString(zabbixTrigger));
        }
    }
}
