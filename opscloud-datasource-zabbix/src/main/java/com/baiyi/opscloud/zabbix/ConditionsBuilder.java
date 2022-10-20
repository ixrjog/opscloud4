package com.baiyi.opscloud.zabbix;

import com.baiyi.opscloud.zabbix.v5.param.ZabbixActionParam;
import com.baiyi.opscloud.zabbix.constant.Conditiontype;
import com.baiyi.opscloud.zabbix.constant.OperatorType;
import com.google.common.collect.Lists;

import java.util.List;

import static com.baiyi.opscloud.common.base.Global.ENV_PROD;

/**
 * @Author baiyi
 * @Date 2021/2/1 11:33 上午
 * @Version 1.0
 */
public class ConditionsBuilder {

    private static final ZabbixActionParam.Condition conditionA = ZabbixActionParam.Condition.builder()
            .conditiontype(Conditiontype.TRIGGER_SEVERITY.getType()) // 4 - trigger severity;
            .operator(OperatorType.IS_GREATER_THAN_OR_EQUALS.getType()) // 5 - is greater than or equals;
            .value("4")
            .formulaid("A")
            .build();

    /**
     * 服务器组
     *
     * @param groupid
     * @return
     */
    private static ZabbixActionParam.Condition buildConditionB(String groupid) {
        return ZabbixActionParam.Condition.builder()
                .conditiontype(Conditiontype.HOST_GROUP.getType()) // 0 - host group;
                .operator(OperatorType.EQUALS.getType()) // 0 - (default) equals;
                .value(groupid)
                .formulaid("B")
                .build();
    }

    /**
     * 环境标签
     */
    private static final ZabbixActionParam.Condition conditionC = ZabbixActionParam.Condition.builder()
            .conditiontype(Conditiontype.EVENT_TAG_VALUE.getType()) // 26 - event tag value.
            .operator(OperatorType.EQUALS.getType()) // 0 - (default) equals;
            .value(ENV_PROD)
            .value2("env")
            .formulaid("C")
            .build();


    /**
     * https://www.zabbix.com/documentation/5.0/manual/api/reference/action/object?s[]=conditions
     * 触发器示警度
     *
     * @param groupid
     * @return
     */
    public static List<ZabbixActionParam.Condition> build(String groupid) {
        return Lists.newArrayList(conditionA, buildConditionB(groupid), conditionC);
    }

}
