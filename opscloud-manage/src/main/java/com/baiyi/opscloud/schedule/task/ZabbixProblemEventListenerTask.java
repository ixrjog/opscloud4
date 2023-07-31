package com.baiyi.opscloud.schedule.task;

import com.baiyi.opscloud.common.annotation.WatchTask;
import com.baiyi.opscloud.config.condition.EnvCondition;
import com.baiyi.opscloud.domain.annotation.InstanceHealth;
import com.baiyi.opscloud.event.IEventHandler;
import com.baiyi.opscloud.event.enums.EventTypeEnum;
import com.baiyi.opscloud.event.factory.EventFactory;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * {@code @Author} baiyi
 * {@code @Date} 2021/10/12 2:32 下午
 * {@code @Version} 1.0
 */
@Slf4j
@Component
@Conditional(EnvCondition.class)
public class ZabbixProblemEventListenerTask {

    @InstanceHealth
    @Scheduled(initialDelay = 8000, fixedRate = 120 * 1000)
    @SchedulerLock(name = "zabbix_problem_event_listener_task", lockAtMostFor = "3m", lockAtLeastFor = "3m")
    @WatchTask(name = "Listen for zabbix problems")
    public void run() {
        IEventHandler iEventProcess = EventFactory.getByEventType(EventTypeEnum.ZABBIX_PROBLEM);
        if (iEventProcess != null) {
            iEventProcess.listener();
        }
    }

}
