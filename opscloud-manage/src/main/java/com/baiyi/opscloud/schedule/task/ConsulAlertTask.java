package com.baiyi.opscloud.schedule.task;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.configuration.condition.EnvCondition;
import com.baiyi.opscloud.facade.task.ConsulAlertFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.CONSUL_ALERT_TASK;

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

    /**
     * @SchedulerLock(name = "consul_alert_rule_evaluate_task", lockAtMostFor = "30s", lockAtLeastFor = "30s")
     */
    @Scheduled(cron = "10 */1 * * * ?")
    @SingleTask(name = CONSUL_ALERT_TASK, lockTime = "1m")
    public void ruleEvaluate() {
        log.info("Consul alert task start");
        consulAlertFacade.ruleEvaluate();
        log.info("Consul alert task end");
    }

}