package com.baiyi.opscloud.common.configuration;

import com.baiyi.opscloud.common.configuration.properties.CacheProperties;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * redis配置类
 *
 * @Author baiyi
 * @Date 2020/1/13 2:31 下午
 * @Version 1.0
 */
@Configuration
@EnableCaching
public class CachingConfiguration {

    public interface Repositories {
        String DEFAULT = "oc4:cache:default:";
        String CACHE_FOR_10S = "oc4:cache:10s:";
        String CACHE_FOR_10M = "oc4:cache:10m:";
        String CACHE_FOR_1H = "oc4:cache:1h:";
        String CACHE_FOR_2H = "oc4:cache:2h:";
        String CACHE_FOR_1D = "oc4:cache:1d:";
        String CACHE_FOR_1W = "oc4:cache:7d:";
    }

    private static final List<CacheProperties.Repo> REPOS = Lists.newArrayList(
            CacheProperties.Repo.DEFAULT,
            CacheProperties.Repo.CACHE_FOR_10S,
            CacheProperties.Repo.CACHE_FOR_10M,
            CacheProperties.Repo.CACHE_FOR_1H,
            CacheProperties.Repo.CACHE_FOR_2H,
            CacheProperties.Repo.CACHE_FOR_1W,
            CacheProperties.Repo.CACHE_FOR_1D
    );

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        // 设置一个初始化的缓存空间set集合
        Set<String> cacheNames = REPOS.stream().map(CacheProperties.Repo::getName).collect(Collectors.toSet());
        // 使用自定义的缓存配置初始化一个cacheManager
        return RedisCacheManager.builder(factory)
                // 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，
                // 再初始化相关的配置
                .initialCacheNames(cacheNames)
                .withInitialCacheConfigurations(getConfigMap())
                .build();
    }

    private Map<String, RedisCacheConfiguration> getConfigMap() {
        Map<String, RedisCacheConfiguration> configMap = Maps.newHashMap();
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        // 设置缓存的默认过期时间，也是使用Duration设置
        config.entryTtl(Duration.ofMinutes(1))
                // 不缓存空值
                .disableCachingNullValues();
        REPOS.forEach(e -> configMap.put(e.getName(), config.entryTtl(e.getTtl())));
        return configMap;
    }

    // ---------------自定义配置项---------------

    /**
     * retemplate相关配置
     *
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);

        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(om, Object.class);
        // 值采用json序列化
        template.setValueSerializer(jackson2JsonRedisSerializer);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());

        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }

    /**
     * 对hash类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    /**
     * 对redis字符串类型数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * 对链表类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * 对无序集合类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * 对有序集合类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

}