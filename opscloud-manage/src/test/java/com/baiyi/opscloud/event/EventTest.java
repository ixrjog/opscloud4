package com.baiyi.opscloud.event;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.event.enums.EventTypeEnum;
import com.baiyi.opscloud.event.factory.EventFactory;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2021/10/9 5:00 下午
 * @Version 1.0
 */
public class EventTest extends BaseUnit {

    @Test
    void zabbixEventProcessListenerTest() {
        IEventProcess iEventProcess = EventFactory.getIEventProcessByEventType(EventTypeEnum.ZABBIX_TRIGGER);
        if (iEventProcess == null) return;
        iEventProcess.listener();
    }
}
