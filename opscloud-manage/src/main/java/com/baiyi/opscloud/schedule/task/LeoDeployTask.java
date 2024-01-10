package com.baiyi.opscloud.schedule.task;

import com.baiyi.opscloud.common.annotation.WatchTask;
import com.baiyi.opscloud.configuration.condition.EnvCondition;
import com.baiyi.opscloud.leo.task.LeoDeployCompensationTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * {@code @Author} baiyi
 * {@code @Date} 2022/12/26 14:26
 * {@code @Version} 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Conditional(EnvCondition.class)
public class LeoDeployTask {

    private final LeoDeployCompensationTask deployCompensationTask;

    /**
     * 间隔2分钟执行一次任务补偿
     */
    //@InstanceHealth
    @Scheduled(initialDelay = 15000, fixedRate = 120 * 1000)
    @WatchTask(name = "Leo deploy compensate")
    public void run() {
        deployCompensationTask.handleTask();
    }

}

