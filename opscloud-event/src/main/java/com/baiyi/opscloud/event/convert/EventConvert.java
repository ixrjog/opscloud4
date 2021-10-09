package com.baiyi.opscloud.event.convert;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.Event;
import com.baiyi.opscloud.event.enums.EventTypeEnum;
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
     * @param zabbixTrigger
     * @return
     */
    public static Event toEvent(DatasourceInstance dsInstance, ZabbixTrigger zabbixTrigger) {
        return Event.builder()
                .instanceUuid(dsInstance.getUuid())
                .eventId(zabbixTrigger.getTriggerid())
                .eventIdDesc(EventTypeEnum.ZABBIX_TRIGGER.name())
                .eventMessage(JSON.toJSONString(zabbixTrigger))
                .priority(zabbixTrigger.getPriority())
                .lastchangeTime(new Date(zabbixTrigger.getLastchange() * 1000))
                .build();
    }


}
