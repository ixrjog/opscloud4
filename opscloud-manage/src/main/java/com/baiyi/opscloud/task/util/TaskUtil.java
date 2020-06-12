package com.baiyi.opscloud.task.util;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.TimeUtils;
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
            redisUtil.set(topic, count + 1, TimeUtils.dayTime * 30);
        } else {
            redisUtil.set(topic, 1, TimeUtils.dayTime * 30);
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

    /**
     * 判断任务是否执行中
     *
     * @param taskKey
     * @return
     */
    public boolean tryLock(String taskKey) {
        return redisUtil.hasKey(taskKey);
    }

    /**
     * 任务锁
     *
     * @param taskKey
     * @param minute  锁定最大时间
     */
    public void lock(String taskKey, int minute) {
        if (minute == 0)
            minute = MAX_MINUTE;
        redisUtil.set(taskKey, true, 60 * minute);
    }

    /**
     * 解除任务锁
     *
     * @param taskKey
     */
    public void unlock(String taskKey) {
        redisUtil.del(taskKey);
    }

}
