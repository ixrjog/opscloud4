package com.baiyi.opscloud.configuration;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Author 修远
 * @Date 2021/7/2 2:44 下午
 * @Since 1.0
 */
@SuppressWarnings("SpringFacetCodeInspection")
@Slf4j
@Configuration
public class InitialRedisConfiguration implements ApplicationContextAware {

    @Resource
    private ConfigurableApplicationContext configurableApplicationContext;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        try {
            RedisTemplate<String, Object> redisTemplate = applicationContext.getBean("redisTemplate", RedisTemplate.class);
            redisTemplate.hasKey("initCheck");
            log.info("Start verification Redis connection succeeded");
        } catch (Exception e) {
            log.error("Start verification Redis unable to connect: {}", e.getMessage());
            /*
             * 停止项目启动
             */
            configurableApplicationContext.close();
        }
    }

}
