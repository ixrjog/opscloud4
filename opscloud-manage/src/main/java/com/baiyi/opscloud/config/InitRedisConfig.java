package com.baiyi.opscloud.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/2 2:44 下午
 * @Since 1.0
 */

@Slf4j
@Configuration
public class InitRedisConfig implements ApplicationContextAware {

    private static ApplicationContext context;

    @Resource
    private ConfigurableApplicationContext configurableApplicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        try {
            context = applicationContext;
            log.info("校验redis连接");
            RedisTemplate<String, Object> redisTemplate = context.getBean("redisTemplate", RedisTemplate.class);
            redisTemplate.hasKey("initCheck");
        } catch (Exception e) {
            log.error("初始化redis连接失败" + e.getMessage());
            // 当检测redis连接失败时, 停止项目启动
            configurableApplicationContext.close();
        }
    }
}
