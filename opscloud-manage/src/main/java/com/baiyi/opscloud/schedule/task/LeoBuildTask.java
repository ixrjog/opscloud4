package com.baiyi.opscloud.schedule.task;

import com.baiyi.opscloud.common.annotation.WatchTask;
import com.baiyi.opscloud.configuration.condition.EnvCondition;
import com.baiyi.opscloud.leo.task.LeoBuildCompensationTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/1/4 14:46
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Conditional(EnvCondition.class)
public class LeoBuildTask {

    private final LeoBuildCompensationTask buildCompensationTask;

    //@InstanceHealth
    @Scheduled(initialDelay = 30000, fixedRate = 180 * 1000)
    @WatchTask(name = "Leo build compensate")
    public void run() {
        buildCompensationTask.handleTask();
    }

}