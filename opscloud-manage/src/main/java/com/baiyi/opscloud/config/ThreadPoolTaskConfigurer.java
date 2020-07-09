package com.baiyi.opscloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

import static com.baiyi.opscloud.common.base.Global.ASYNC_POOL_TASK_COMMON;
import static com.baiyi.opscloud.common.base.Global.ASYNC_POOL_TASK_EXECUTOR;

/**
 * @Author baiyi
 * @Date 2020/3/30 1:21 下午
 * @Version 1.0
 */
@EnableAsync
@Configuration
public class ThreadPoolTaskConfigurer {
    // https://blog.csdn.net/CJ_66/article/details/82503665
    // https://blog.csdn.net/xie19900123/article/details/81771793

    /** 核心线程数（默认线程数） */
    private static final int corePoolSize = 50;
    /** 最大线程数 */
    private static final int maxPoolSize = 100;
    /** 允许线程空闲时间（单位：默认为秒） */
    private static final int keepAliveTime = 60;
    /** 缓冲队列大小 */
    private static final int queueCapacity = 200;
    /** 线程池名前缀 */
    private static final String threadNamePrefix = "Async-Service-";

    @Bean(name = ASYNC_POOL_TASK_EXECUTOR)
    public ThreadPoolTaskExecutor getAsyncPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        /**
         * 那么poolSize、corePoolSize、maximumPoolSize三者的关系是如何的呢？
         * 当新提交一个任务时：
         * （1）如果poolSize<corePoolSize，新增加一个线程处理新的任务。
         * （2）如果poolSize=corePoolSize，新任务会被放入阻塞队列等待。
         * （3）如果阻塞队列的容量达到上限，且这时poolSize<maximumPoolSize，新增线程来处理任务。
         * （4）如果阻塞队列满了，且poolSize=maximumPoolSize，那么线程池已经达到极限，会根据饱和策略RejectedExecutionHandler拒绝新的任务。
         *
         * 所以通过上面的描述可知corePoolSize<=maximumPoolSize，poolSize<=maximumPoolSize；而poolSize和corePoolSize无法比较，poolSize是有可能比corePoolSize大的。
         */
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveTime);
        executor.setThreadNamePrefix(threadNamePrefix);
        // 而在一些场景下，若需要在关闭线程池时等待当前调度任务完成后才开始关闭，可以通过简单的配置，进行优雅的停机策略配置。关键就是通过
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 线程池对拒绝任务的处理策略
        // CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();

        return executor;
    }

    @Bean(name = ASYNC_POOL_TASK_COMMON)
    public ThreadPoolTaskExecutor getAsyncPoolTaskCommon() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        /**
         * 那么poolSize、corePoolSize、maximumPoolSize三者的关系是如何的呢？
         * 当新提交一个任务时：
         * （1）如果poolSize<corePoolSize，新增加一个线程处理新的任务。
         * （2）如果poolSize=corePoolSize，新任务会被放入阻塞队列等待。
         * （3）如果阻塞队列的容量达到上限，且这时poolSize<maximumPoolSize，新增线程来处理任务。
         * （4）如果阻塞队列满了，且poolSize=maximumPoolSize，那么线程池已经达到极限，会根据饱和策略RejectedExecutionHandler拒绝新的任务。
         *
         * 所以通过上面的描述可知corePoolSize<=maximumPoolSize，poolSize<=maximumPoolSize；而poolSize和corePoolSize无法比较，poolSize是有可能比corePoolSize大的。
         */
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveTime);
        executor.setThreadNamePrefix(threadNamePrefix);
        // 而在一些场景下，若需要在关闭线程池时等待当前调度任务完成后才开始关闭，可以通过简单的配置，进行优雅的停机策略配置。关键就是通过
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 线程池对拒绝任务的处理策略
        // CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();

        return executor;
    }

}
