package com.baiyi.opscloud.config;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static com.baiyi.opscloud.common.config.ThreadPoolTaskConfiguration.TaskPools.CORE;


/**
 * @Author 修远
 * @Date 2021/6/15 5:42 下午
 * @Since 1.0
 */

@Configuration
@RequiredArgsConstructor
public class EventBusConfiguration {

    private final ApplicationContext applicationContext;

    @Bean
    public EventBus eventBus() {
        return new EventBus();
    }

    @Bean
    public AsyncEventBus asyncEventBus() {
        ThreadPoolTaskExecutor executor = applicationContext.getBean(CORE, ThreadPoolTaskExecutor.class);
        return new AsyncEventBus(executor);
    }
}
