package com.baiyi.opscloud.schedule.task;

import com.baiyi.opscloud.config.condition.EnvCondition;
import com.baiyi.opscloud.facade.task.ConsulAlertFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author 修远
 * @Date 2022/7/21 5:14 PM
 * @Since 1.0
 */
@Slf4j
@Component
@Conditional(EnvCondition.class)
@RequiredArgsConstructor
public class ConsulAlertTask  {

    private final ConsulAlertFacade consulAlertFacade;

    @Scheduled(cron = "10 */1 * * * ?")
    @SchedulerLock(name = "consul_alert_rule_evaluate_task", lockAtMostFor = "30s", lockAtLeastFor = "30s")
    public void ruleEvaluate() {
        log.info("Consul alert task start");
        consulAlertFacade.ruleEvaluate();
        log.info("Consul alert task end");
    }

}
