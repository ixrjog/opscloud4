package com.baiyi.opscloud.task;

import com.baiyi.opscloud.domain.annotation.InstanceHealth;
import com.baiyi.opscloud.event.IEventProcess;
import com.baiyi.opscloud.event.enums.EventTypeEnum;
import com.baiyi.opscloud.event.factory.EventFactory;
import com.baiyi.opscloud.facade.sys.InstanceFacade;
import com.baiyi.opscloud.task.base.BaseTask;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/10/12 2:32 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ZabbixProblemEventListenerTask extends BaseTask {

    @Resource
    private InstanceFacade instanceFacade;

    @InstanceHealth // 实例健康检查，高优先级
    @SchedulerLock(name = "zabbix_problem_event_listener_task", lockAtMostFor = "1m", lockAtLeastFor = "1m")
    @Scheduled(initialDelay = 10000, fixedRate = 60 * 1000)
    public void listenerTask() {
        IEventProcess iEventProcess = EventFactory.getIEventProcessByEventType(EventTypeEnum.ZABBIX_PROBLEM);
        if (iEventProcess == null) return;
        log.info("定时任务开始: 监听zabbix问题！");
        iEventProcess.listener();
        log.info("定时任务开始: 监听zabbix问题！");
    }


}
