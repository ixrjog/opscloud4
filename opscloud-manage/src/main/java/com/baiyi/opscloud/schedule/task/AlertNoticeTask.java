package com.baiyi.opscloud.schedule.task;

import com.baiyi.opscloud.domain.annotation.InstanceHealth;
import com.baiyi.opscloud.event.alert.AlertHandler;
import com.baiyi.opscloud.schedule.task.base.AbstractTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.baiyi.opscloud.common.base.Global.ENV_PROD;

/**
 * @Author baiyi
 * @Date 2022/5/31 13:23
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AlertNoticeTask extends AbstractTask {

    private final AlertHandler alertHandler;

    @InstanceHealth // 实例健康检查，高优先级
    @Scheduled(initialDelay = 12000, fixedRate = 60 * 1000)
    @SchedulerLock(name = "alert_notice_task_1", lockAtMostFor = "1m", lockAtLeastFor = "1m")
    public void alertNoticeTask() {
        // 非生产环境不执行任务
        if (ENV_PROD.equals(env)) {
            log.info("定时任务开始: 处理告警任务！");
            alertHandler.sendTask();
            log.info("定时任务结束: 处理告警任务！");
        }
    }

}
