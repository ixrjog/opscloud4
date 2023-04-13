package com.baiyi.opscloud.event;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.event.consumer.impl.kind.EmployeeResignConsumer;
import com.baiyi.opscloud.event.enums.EventTypeEnum;
import com.baiyi.opscloud.event.factory.EventFactory;
import com.baiyi.opscloud.service.user.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2021/10/9 5:00 下午
 * @Version 1.0
 */
public class EventTest extends BaseUnit {

    @Resource
    private EmployeeResignConsumer employeeResignConsumer;

    @Resource
    private UserService userService;

    @Test
    void zabbixEventProcessListenerTest() {
        IEventHandler iEventProcess = EventFactory.getByEventType(EventTypeEnum.ZABBIX_PROBLEM);
        if (iEventProcess == null) return;
        iEventProcess.listener();
    }

    @Test
    void employeeResignConsumerTest() {
        User user = userService.getByUsername("baiyitest");
        employeeResignConsumer.generateTicket(user);
    }

}
