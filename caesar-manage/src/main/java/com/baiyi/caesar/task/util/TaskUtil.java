package com.baiyi.caesar.task.util;

import com.baiyi.caesar.common.redis.RedisUtil;
import com.baiyi.caesar.common.util.TimeUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/4/10 4:12 下午
 * @Version 1.0
 */
@Component
public class TaskUtil {

    @Resource
    private RedisUtil redisUtil;

    public static final int MAX_MINUTE = 30;

    // 增加统计次数
    public void sendMessage(String topic) {
        if (redisUtil.hasKey(topic)) {
            int count = (int) redisUtil.get(topic);
            redisUtil.set(topic, count + 1, TimeUtil.dayTime * 30);
        } else {
            redisUtil.set(topic, 1, TimeUtil.dayTime * 30);
        }
    }

    public int getSignalCount(String taskSignal) {
        if (redisUtil.hasKey(taskSignal)) {
            return (int) redisUtil.get(taskSignal);
        } else {
            return 0;
        }
    }

    // 清除统计次数
    public void clearSignalCount(String taskSignal) {
        redisUtil.del(taskSignal);
    }



}
