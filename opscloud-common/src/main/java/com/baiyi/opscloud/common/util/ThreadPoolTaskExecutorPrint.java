package com.baiyi.opscloud.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author baiyi
 * @Date 2022/6/28 15:32
 * @Version 1.0
 */
@Slf4j
public class ThreadPoolTaskExecutorPrint {

    public static void print(ThreadPoolTaskExecutor threadPoolTaskExecutor,String name) {
        ThreadPoolExecutor threadPoolExecutor = threadPoolTaskExecutor.getThreadPoolExecutor();
        log.info("{} threadPoolExecutor: taskCount = {}, completedTaskCount = {}, poolSize = {}, activeCount = {}",
                name,
                threadPoolExecutor.getTaskCount(),
                threadPoolExecutor.getCompletedTaskCount(),
                threadPoolExecutor.getPoolSize(),
                threadPoolExecutor.getActiveCount());
    }

}
