package com.sdg.cmdb.service;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class CacheKeyService {

    private static final String perfKey = "Def";

    @Resource
    private RedisTemplate redisTemplate;

    public static final String PROJECT_TASK_LOCK_KEY = "CacheKeyService:ProjectTaskLockKey:Status";

    /**
     * 用于单任务锁
     *
     * @param key
     * @return
     */
    public boolean checkRunning(String key) {
        String isRunning = this.getKeyByString(key);
        if (isRunning != null && isRunning.equalsIgnoreCase("true"))
            return true;
        return false;
    }

    /**
     * 用于单任务锁
     *
     * @param key
     */
    public void setRunning(String key, boolean isRunning) {
        if (isRunning) {
            set(key, "true", 20);
        } else {
            set(key, "false");
        }
    }


    public String getKeyByString(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) return null;
        String obj = value.toString();
        return obj;
    }

    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存N分钟
     *
     * @param key
     * @param value
     * @param minutes
     */
    public void set(String key, String value, int minutes) {
        redisTemplate.opsForValue().set(key, value, minutes, TimeUnit.MINUTES);
    }

    /**
     * 插入指定key-value    有效期20分钟
     */
    private void demoInsertForTime() {
        redisTemplate.opsForValue().set("key", "value", 20, TimeUnit.MINUTES);
    }

    /**
     * 删除指定key
     */
    private void demoDel() {
        redisTemplate.delete("key");
    }

}
