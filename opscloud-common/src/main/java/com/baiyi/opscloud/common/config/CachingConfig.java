package com.baiyi.opscloud.common.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2020/1/13 2:31 下午
 * @Version 1.0
 */
@Configuration
@EnableCaching
public class CachingConfig extends CachingConfigurerSupport {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        // 设置一个初始化的缓存空间set集合
        Set<String> cacheNames = new HashSet<>();
        cacheNames.add("instanceTypeContext");
        cacheNames.add("aliyunECSDisk");
        cacheNames.add("aliyunECSRenew");
        // 使用自定义的缓存配置初始化一个cacheManager
        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                // 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，
                // 再初始化相关的配置
                .initialCacheNames(cacheNames).withInitialCacheConfigurations(getConfigMap()).build();
        return cacheManager;
    }

    private Map<String, RedisCacheConfiguration> getConfigMap(){
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        // 设置缓存的默认过期时间，也是使用Duration设置
        config = config.entryTtl(Duration.ofMinutes(1))
                // 不缓存空值
                .disableCachingNullValues();
        configMap.put("instanceTypeContext", config.entryTtl(Duration.ofMinutes(2)));
        configMap.put("aliyunECSDisk", config.entryTtl(Duration.ofMinutes(2)));
        configMap.put("aliyunECSRenew", config.entryTtl(Duration.ofMinutes(2)));
        return configMap;
    }


}