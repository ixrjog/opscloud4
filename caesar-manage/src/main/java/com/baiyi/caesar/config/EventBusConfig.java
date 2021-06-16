package com.baiyi.caesar.config;

import com.baiyi.caesar.common.base.Global;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;


/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/15 5:42 下午
 * @Since 1.0
 */

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
        ThreadPoolTaskExecutor executor = applicationContext.getBean(Global.TaskPools.COMMON, ThreadPoolTaskExecutor.class);
        return new AsyncEventBus(executor);
    }
}
