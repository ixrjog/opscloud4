package com.baiyi.opscloud.datasource.zabbix;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.datasource.zabbix.base.BaseZabbixTest;
import com.baiyi.opscloud.zabbix.entry.ZabbixProblem;
import com.baiyi.opscloud.zabbix.entry.ZabbixTrigger;
import com.baiyi.opscloud.zabbix.handler.ZabbixProblemHandler;
import com.baiyi.opscloud.zabbix.handler.ZabbixTriggerHandler;
import com.baiyi.opscloud.zabbix.param.base.SeverityType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/10/11 3:16 下午
 * @Version 1.0
 */
public class ZabbixProblemTest extends BaseZabbixTest {

    @Resource
    private ZabbixProblemHandler zabbixProblemHandler;

    @Resource
    private ZabbixTriggerHandler zabbixTriggerHandler;

    @Test
    void listProblemTest() {
        List<ZabbixProblem> problems = zabbixProblemHandler.list(getConfig().getZabbix(), com.google.common.collect.Lists.newArrayList(SeverityType.HIGH, SeverityType.DISASTER));
        List<ZabbixTrigger> triggers = Lists.newArrayList();
        System.out.println("size = " + problems.size());
        for (ZabbixProblem problem : problems) {
            System.err.println(JSON.toJSONString(problem));
            ZabbixTrigger trigger = zabbixTriggerHandler.getById(getConfig().getZabbix(), problem.getObjectid());
            if (trigger != null) {
                System.err.println(JSON.toJSONString(trigger));
                triggers.add(trigger);
            }
            System.err.println("");
        }
        System.out.println("triggerSize = " + triggers.size());
    }

    @Test
    void getProblemTest() {
        ZabbixProblem problem = zabbixProblemHandler.getByTriggerId(getConfig().getZabbix(), "29441");
        System.err.println(JSON.toJSONString(problem));
    }
}
