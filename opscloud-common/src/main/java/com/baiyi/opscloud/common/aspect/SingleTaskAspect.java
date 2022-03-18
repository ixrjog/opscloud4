package com.baiyi.opscloud.common.aspect;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.InstantUtil;
import com.baiyi.opscloud.common.util.StringToDurationUtil;
import com.baiyi.opscloud.domain.ErrorEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

/**
 * @Author baiyi
 * @Date 2021/6/10 3:50 下午
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Order(1)
public class SingleTaskAspect {

    private static final int RUNNING = 1;

    private final RedisUtil redisUtil;

    private String buildKey(String taskName) {
        return String.format("Opscloud.V4.SingleTask.%s", taskName);
    }

    @Pointcut(value = "@annotation(com.baiyi.opscloud.common.annotation.SingleTask)")
    public void annotationPoint() {
    }

    @Before("annotationPoint()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
    }

    @Around("@annotation(singleTask)")
    public Object around(ProceedingJoinPoint joinPoint, SingleTask singleTask) throws Throwable {
        String key = buildKey(singleTask.name());
        try {
            if (!isLocked(key)) {
                log.info("同步任务开始，taskKey = {}", key);
                Instant instant = Instant.now();
                lock(key, singleTask.lockTime());
                Object result = joinPoint.proceed();
                unlocking(key);
                log.info("同步任务结束，taskKey = {}, 消耗时间 = {}s", key, InstantUtil.timerSeconds(instant));
                return result;
            } else {
                log.info("任务重复执行: taskKey = {} !", key);
                return new CommonRuntimeException(ErrorEnum.SINGLE_TASK_RUNNING);
            }
        } catch (Exception e) {
            e.printStackTrace();
            unlocking(key);
        } finally {
            unlocking(key);
        }
        return new Throwable();
    }

    private void lock(String lockKey, String time) {
        Duration duration = StringToDurationUtil.parse(time);
        redisUtil.set(lockKey, RUNNING, duration.getSeconds());
    }

    private void unlocking(String lockKey) {
        redisUtil.del(lockKey);
    }

    private boolean isLocked(String lockKey) {
        return redisUtil.get(lockKey) != null;
    }

}
