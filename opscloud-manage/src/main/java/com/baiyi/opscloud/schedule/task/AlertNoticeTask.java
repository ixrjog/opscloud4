package com.baiyi.opscloud.schedule.task;

import com.baiyi.opscloud.config.condition.EnvCondition;
import com.baiyi.opscloud.event.alert.AlertHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/5/31 13:23
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
// 非生产环境不执行
@Conditional(EnvCondition.class)
public class AlertNoticeTask {

    private final AlertHandler alertHandler;

    @Scheduled(initialDelay = 12000, fixedRate = 60 * 1000)
    @SchedulerLock(name = "alert_notice_task", lockAtMostFor = "50s", lockAtLeastFor = "50s")
    public void run() {
        log.info("Alert notice task start");
        alertHandler.sendTask();
        log.info("Alert notice task end");
    }

}
