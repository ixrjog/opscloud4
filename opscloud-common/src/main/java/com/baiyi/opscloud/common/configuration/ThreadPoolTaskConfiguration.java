package com.baiyi.opscloud.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

import static com.baiyi.opscloud.common.configuration.ThreadPoolTaskConfiguration.TaskPools.*;


/**
 * @Author baiyi
 * @Date 2020/3/30 1:21 下午
 * @Version 1.0
 */
@Configuration
public class ThreadPoolTaskConfiguration {

    public interface TaskPools {
        String CORE = "coreExecutor";
        String SERVER_TERMINAL = "serverTerminalExecutor";
        String KUBERNETES_TERMINAL = "kubernetesTerminalExecutor";
        String LEO = "leoExecutor";
    }

    /**
     * 允许线程空闲时间（单位：默认为秒）
     */
    private static final int KEEP_ALIVE_TIME = 60;
    /**
     * 缓冲队列大小
     */
    private static final int QUEUE_CAPACITY = 0;
    /**
     * 线程池名前缀
     */
    private static final String THREAD_NAME_PREFIX = "core-exec-";

    @Bean(name = CORE)
    public ThreadPoolTaskExecutor coreExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(200);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
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

    @Bean(name = LEO)
    public ThreadPoolTaskExecutor leoExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(400);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setThreadNamePrefix("leo-exec-");
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

    @Bean(name = SERVER_TERMINAL)
    public ThreadPoolTaskExecutor serverTerminalExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(150);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setThreadNamePrefix("st-exec-");
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

    @Bean(name = KUBERNETES_TERMINAL)
    public ThreadPoolTaskExecutor kubernetesTerminalExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(150);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setThreadNamePrefix("kt-exec-");
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