package com.baiyi.opscloud.redis;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.redis.RedisUtil;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/3/8 12:57 下午
 * @Version 1.0
 */
public class RedisUtilTest extends BaseUnit {


    @Resource
    private RedisUtil redisUtil;

    @Test
    void testCache() {
        String key = "REDIS_TEST";
        String s = "123456";
        redisUtil.set(key, s, 100);

        System.err.println(redisUtil.get(key));
    }

}
