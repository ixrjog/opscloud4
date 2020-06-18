package com.baiyi.opscloud.common.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/3/8 12:52 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class CacheRepository {
    @Resource
    private RedisUtil redisUtil;
}
