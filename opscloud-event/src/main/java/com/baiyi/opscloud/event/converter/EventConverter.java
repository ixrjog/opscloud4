package com.baiyi.opscloud.event.converter;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.Event;
import com.baiyi.opscloud.event.enums.EventTypeEnum;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixProblem;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixTrigger;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/10/9 3:29 下午
 * @Version 1.0
 */
public class EventConverter {

    /**
     * 事件转换 ZabbixTrigger to Event
     *
     * @param dsInstance
     * @param trigger
     * @return
     */
    public static Event toEvent(DatasourceInstance dsInstance, ZabbixProblem.Problem problem, ZabbixTrigger.Trigger trigger) {
        if (trigger == null) {
            return toEvent(dsInstance, problem);
        }
        return Event.builder()
                .instanceUuid(dsInstance.getUuid())
                .eventId(problem.getEventid())
                .eventName(problem.getName())
                .eventIdDesc(EventTypeEnum.ZABBIX_PROBLEM.name())
                .eventMessage(JSONUtil.writeValueAsString(problem))
                .priority(problem.getSeverity())
                // UNIT时间戳转换
                .createTime(new Date(problem.getClock() * 1000))
                // UNIT时间戳转换
                .lastchangeTime(new Date(trigger.getLastchange() * 1000))
                // 有效事件
                .isActive(!problem.isRecover())
                .build();
    }

    public static Event toEvent(DatasourceInstance dsInstance, ZabbixProblem.Problem problem) {
        return Event.builder()
                .instanceUuid(dsInstance.getUuid())
                .eventName(problem.getName())
                .eventId(problem.getEventid())
                .eventIdDesc(EventTypeEnum.ZABBIX_PROBLEM.name())
                .eventMessage(JSONUtil.writeValueAsString(problem))
                .priority(problem.getSeverity())
                // UNIT时间戳转换
                .createTime(new Date(problem.getClock() * 1000))
                // 有效事件
                .isActive(false)
                .build();
    }

}