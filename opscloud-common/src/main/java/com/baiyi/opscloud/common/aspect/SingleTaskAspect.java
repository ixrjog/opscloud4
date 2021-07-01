package com.baiyi.opscloud.common.aspect;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.domain.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/6/10 3:50 下午
 * @Version 1.0
 */
@Aspect
@Component
@Slf4j
public class SingleTaskAspect {

    private static final int RUNNING = 1;

    @Resource
    private RedisUtil redisUtil;

    private String buildKey(String taskName) {
        return String.format("Opscloud.V4.SingleTask.%s.Running", taskName);
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
                lock(key, singleTask.lockTime());
                joinPoint.proceed();
                unlocking(key);
            } else {
                log.info("任务重复执行: taskKey = {} !", key);
                return new CommonRuntimeException(ErrorEnum.SINGLE_TASK_RUNNING);
            }
        } catch (Exception e) {
            unlocking(key);
            return new Throwable();
        }
        return joinPoint;
    }

    private void lock(String lockKey, long time) {
        redisUtil.set(lockKey, RUNNING, time);
    }

    private void unlocking(String lockKey) {
        redisUtil.del(lockKey);
    }

    private boolean isLocked(String lockKey) {
        return redisUtil.get(lockKey) != null;
    }

}
