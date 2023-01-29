package com.baiyi.opscloud.schedule.task;

import cn.hutool.core.date.StopWatch;
import com.baiyi.opscloud.config.condition.EnvCondition;
import com.baiyi.opscloud.domain.annotation.InstanceHealth;
import com.baiyi.opscloud.event.alert.AlertHandler;
import com.baiyi.opscloud.schedule.task.base.AbstractTask;
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
public class AlertNoticeTask extends AbstractTask {

    private final AlertHandler alertHandler;

    @InstanceHealth // 实例健康检查，高优先级
    @Scheduled(initialDelay = 12000, fixedRate = 60 * 1000)
    @SchedulerLock(name = "alert_notice_task", lockAtMostFor = "1m", lockAtLeastFor = "1m")
    public void run() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("Scheduled task: 处理告警任务！");
        alertHandler.sendTask();
        stopWatch.stop();
        log.info(stopWatch.shortSummary());
    }

}
