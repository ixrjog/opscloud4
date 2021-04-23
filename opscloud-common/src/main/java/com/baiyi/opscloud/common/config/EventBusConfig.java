package com.baiyi.opscloud.common.config;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import lombok.Data;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;

import static com.baiyi.opscloud.common.base.Global.ASYNC_POOL_TASK_COMMON;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/22 3:58 下午
 * @Since 1.0
 */
@Data
@Configuration
public class EventBusConfig {

    @Resource
    private ApplicationContext applicationContext;

    @Bean
    public EventBus eventBus() {
        return new EventBus();
    }

    @Bean
    public AsyncEventBus asyncEventBus() {
        ThreadPoolTaskExecutor executor = applicationContext.getBean(ASYNC_POOL_TASK_COMMON, ThreadPoolTaskExecutor.class);
        return new AsyncEventBus(executor);
    }
}
