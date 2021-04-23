package com.baiyi.opscloud.zabbix.builder;

import com.baiyi.opscloud.zabbix.base.Conditiontype;
import com.baiyi.opscloud.zabbix.base.OperatorType;
import com.baiyi.opscloud.zabbix.param.ZabbixActionParam;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/2/1 11:33 上午
 * @Version 1.0
 */
public class ConditionsBuilder {

    public static List<ZabbixActionParam.Condition> build(String groupid) {
        List<ZabbixActionParam.Condition> conditions = Lists.newArrayList();

        /**
         * https://www.zabbix.com/documentation/5.0/manual/api/reference/action/object?s[]=conditions
         * 	触发器示警度
         */
        ZabbixActionParam.Condition conditionA = ZabbixActionParam.Condition.builder()
                .conditiontype(Conditiontype.TRIGGER_SEVERITY.getType()) // 4 - trigger severity;
                .operator(OperatorType.IS_GREATER_THAN_OR_EQUALS.getType()) // 5 - is greater than or equals;
                .value("4")
                .formulaid("A")
                .build();
        conditions.add(conditionA);
        // 服务器组
        ZabbixActionParam.Condition conditionB = ZabbixActionParam.Condition.builder()
                .conditiontype(Conditiontype.HOST_GROUP.getType()) // 0 - host group;
                .operator(OperatorType.EQUALS.getType()) // 0 - (default) equals;
                .value(groupid)
                .formulaid("B")
                .build();
        conditions.add(conditionB);
        // 环境标签
        ZabbixActionParam.Condition conditionC = ZabbixActionParam.Condition.builder()
                .conditiontype(Conditiontype.EVENT_TAG_VALUE.getType()) // 26 - event tag value.
                .operator(OperatorType.EQUALS.getType()) // 0 - (default) equals;
                .value("prod")
                .value2("env")
                .formulaid("C")
                .build();
        conditions.add(conditionC);
        return conditions;
    }
}
