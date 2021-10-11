package com.baiyi.opscloud.event.convert;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.Event;
import com.baiyi.opscloud.event.enums.EventTypeEnum;
import com.baiyi.opscloud.zabbix.entry.ZabbixProblem;
import com.baiyi.opscloud.zabbix.entry.ZabbixTrigger;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/10/9 3:29 下午
 * @Version 1.0
 */
public class EventConvert {

    /**
     * 事件转换 ZabbixTrigger to Event
     *
     * @param dsInstance
     * @param trigger
     * @return
     */
    public static Event toEvent(DatasourceInstance dsInstance, ZabbixProblem problem, ZabbixTrigger trigger) {
        if (trigger == null) return toEvent(dsInstance, problem);
        return Event.builder()
                .instanceUuid(dsInstance.getUuid())
                .eventId(problem.getEventid())
                .eventIdDesc(EventTypeEnum.ZABBIX_PROBLEM.name())
                .eventMessage(JSON.toJSONString(problem))
                .priority(problem.getSeverity())
                .createTime(new Date(problem.getClock() * 1000)) // UNIT时间戳转换
                .lastchangeTime(new Date(trigger.getLastchange() * 1000)) // UNIT时间戳转换
                .isActive(true) // 有效事件
                .build();
    }

    public static Event toEvent(DatasourceInstance dsInstance, ZabbixProblem problem) {
        return Event.builder()
                .instanceUuid(dsInstance.getUuid())
                .eventId(problem.getEventid())
                .eventIdDesc(EventTypeEnum.ZABBIX_PROBLEM.name())
                .eventMessage(JSON.toJSONString(problem))
                .priority(problem.getSeverity())
                .createTime(new Date(problem.getClock() * 1000)) // UNIT时间戳转换
                .isActive(false) // 有效事件
                .build();
    }


}
