package com.baiyi.opscloud.common.aspect;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.redis.RedisUtil;
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
import org.springframework.util.StopWatch;

import java.time.Duration;

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
        return String.format("opscloud.v4.singleTask#taskName=%s", taskName);
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
            if (isLocked(key)) {
                log.warn("Asset synchronization task repeat: taskKey={}", key);
                return new OCException(ErrorEnum.SINGLE_TASK_RUNNING);
            } else {
                // 加锁
                lock(key, singleTask.lockTime());
                log.info("Asset synchronization task start: taskKey={}", key);
                StopWatch stopWatch = new StopWatch();
                stopWatch.start("Asset synchronization task");
                Object result = joinPoint.proceed();
                unlock(key);
                stopWatch.stop();
                log.info("Asset synchronization task end: taskKey={}, runtime={}/s", key, stopWatch.getTotalTimeSeconds());
                return result;
            }
        } catch (Exception e) {
            log.error("Asset synchronization task error: {}", e.getMessage());
            // 解锁
            unlock(key);
        }
        return new Throwable();
    }

    private void lock(String lockKey, String time) {
        Duration duration = StringToDurationUtil.parse(time);
        redisUtil.set(lockKey, RUNNING, duration.getSeconds());
    }

    private void unlock(String lockKey) {
        redisUtil.del(lockKey);
    }

    private boolean isLocked(String lockKey) {
        return redisUtil.get(lockKey) != null;
    }

}
