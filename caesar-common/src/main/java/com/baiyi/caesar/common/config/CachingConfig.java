package com.baiyi.caesar.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * redis配置类
 *
 * @Author baiyi
 * @Date 2020/1/13 2:31 下午
 * @Version 1.0
 */
@Configuration
@EnableCaching
public class CachingConfig extends CachingConfigurerSupport {


    public interface CacheRepositories {
        String COMMON = "Caesar:Common";
        String ANSIBLE = "Caesar:AnsibleRepo";
        String ATTRIBUTE = "Caesar:Attribute";
        String SETTING = "Caesar:Setting";
        String ENGINE_CHART = "Caesar:EngineChart";
        String DASHBOARD = "Caesar:Dashboard";
        String JOB_RUN_NODES = "Caesar:JobRunNodes";
    }


    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        // 设置一个初始化的缓存空间set集合
        Set<String> cacheNames = new HashSet<>();
        cacheNames.add(CacheRepositories.COMMON);
        cacheNames.add(CacheRepositories.ANSIBLE);
        cacheNames.add(CacheRepositories.ATTRIBUTE);
        cacheNames.add(CacheRepositories.DASHBOARD);
        cacheNames.add(CacheRepositories.SETTING);
        cacheNames.add(CacheRepositories.ENGINE_CHART);
        cacheNames.add(CacheRepositories.JOB_RUN_NODES);
        // 使用自定义的缓存配置初始化一个cacheManager
        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                // 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，
                // 再初始化相关的配置
                .initialCacheNames(cacheNames).withInitialCacheConfigurations(getConfigMap()).build();
        return cacheManager;
    }

    private Map<String, RedisCacheConfiguration> getConfigMap() {
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        // 设置缓存的默认过期时间，也是使用Duration设置
        config = config.entryTtl(Duration.ofMinutes(1))
                // 不缓存空值
                .disableCachingNullValues();
        configMap.put(CacheRepositories.COMMON, config.entryTtl(Duration.ofMinutes(10)));
        configMap.put(CacheRepositories.ANSIBLE, config.entryTtl(Duration.ofDays(7)));
        configMap.put(CacheRepositories.ATTRIBUTE, config.entryTtl(Duration.ofDays(7)));
        configMap.put(CacheRepositories.SETTING, config.entryTtl(Duration.ofMinutes(10)));
        configMap.put(CacheRepositories.ENGINE_CHART, config.entryTtl(Duration.ofSeconds(5)));
        configMap.put(CacheRepositories.DASHBOARD, config.entryTtl(Duration.ofMinutes(1)));
        configMap.put(CacheRepositories.JOB_RUN_NODES,config.entryTtl(Duration.ofMinutes(10)));
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

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSeial.setObjectMapper(om);

        // 值采用json序列化
        template.setValueSerializer(jacksonSeial);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());

        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jacksonSeial);
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