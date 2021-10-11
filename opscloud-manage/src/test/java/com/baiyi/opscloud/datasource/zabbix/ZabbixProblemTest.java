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
        /**
         *  {"description":"High memory utilization ( >90% for 5m)","hosts":[{"hostid":"11066"}],"lastchange":1633401495,"priority":4,"recover":false,"triggerid":"29441","value":1}
         * {"description":"community-web-daily-1 Springboot服务已停止","hosts":[{"hostid":"11403"}],"lastchange":1625818863,"priority":4,"recover":false,"triggerid":"42758","value":1}
         * {"description":"webStatus检测异常","hosts":[{"hostid":"11403"}],"lastchange":1625818954,"priority":4,"recover":false,"triggerid":"42759","value":1}
         * {"description":"High memory utilization ( >90% for 5m)","hosts":[{"hostid":"11044"}],"lastchange":1627229164,"priority":4,"recover":false,"triggerid":"28957","value":1}
         * {"description":"High memory utilization ( >90% for 5m)","hosts":[{"hostid":"11184"}],"lastchange":1629947103,"priority":4,"recover":false,"triggerid":"32037","value":1}
         * {"description":"shoppingcart-daily-1 Springboot服务已停止","hosts":[{"hostid":"11135"}],"lastchange":1626345354,"priority":4,"recover":false,"triggerid":"43574","value":1}
         * {"description":"webStatus检测异常","hosts":[{"hostid":"11135"}],"lastchange":1626345435,"priority":4,"recover":false,"triggerid":"43575","value":1}
         * {"description":"bcoin-daily-1 Springboot服务已停止","hosts":[{"hostid":"11138"}],"lastchange":1626349459,"priority":4,"recover":false,"triggerid":"42565","value":1}
         * {"description":"webStatus检测异常","hosts":[{"hostid":"11138"}],"lastchange":1626349559,"priority":4,"recover":false,"triggerid":"42566","value":1}
         * {"description":"High memory utilization ( >90% for 5m)","hosts":[{"hostid":"11035"}],"lastchange":1632481068,"priority":4,"recover":false,"triggerid":"28751","value":1}
         * {"description":"/: Disk space is low (used > 80%)","hosts":[{"hostid":"11838"}],"lastchange":1633936654,"priority":4,"recover":false,"triggerid":"56656","value":1}
         */
        ZabbixProblem problem = zabbixProblemHandler.getByTriggerId(getConfig().getZabbix(), "29441");
        System.err.println(JSON.toJSONString(problem));
    }
}
