package com.baiyi.opscloud.task;

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

    /**
     * 判断任务是否执行中
     *
     * @param taskKey
     * @return
     */
    public boolean isTaskLock(String taskKey) {
        return redisUtil.hasKey(taskKey);
    }

    /**
     * 任务锁
     * @param taskKey
     * @param minute 锁定最大时间
     */
    public void setTaskLock(String taskKey, int minute) {
        if (minute == 0)
            minute = MAX_MINUTE;
        redisUtil.set(taskKey, true, TimeUtils.minuteTime * minute);
    }

    /**
     * 解除任务锁
     * @param taskKey
     */
    public void delTaskLock(String taskKey) {
        redisUtil.del(taskKey);
    }

}
