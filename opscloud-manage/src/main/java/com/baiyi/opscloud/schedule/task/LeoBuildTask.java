package com.baiyi.opscloud.schedule.task;

import cn.hutool.core.date.StopWatch;
import com.baiyi.opscloud.domain.annotation.InstanceHealth;
import com.baiyi.opscloud.leo.task.LeoDeployCompensationTask;
import com.baiyi.opscloud.schedule.task.base.AbstractTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.baiyi.opscloud.common.base.Global.ENV_PROD;

/**
 * @Author baiyi
 * @Date 2023/1/4 14:46
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LeoBuildTask extends AbstractTask {

    private final LeoDeployCompensationTask leoDeployCompensationTask;

    @InstanceHealth // 实例健康检查，高优先级
    @Scheduled(initialDelay = 30000, fixedRate = 180 * 1000)
    public void listenerTask() {
        // 非生产环境不执行任务
        if (ENV_PROD.equals(env)) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start("LeoBuild补偿任务执行");
            leoDeployCompensationTask.handleTask();
            stopWatch.stop();
            log.info(stopWatch.shortSummary());
        }
    }

}

