package com.baiyi.caesar.common.aspect;

import com.baiyi.caesar.common.annotation.SingleTask;
import com.baiyi.caesar.common.exception.common.CommonRuntimeException;
import com.baiyi.caesar.common.redis.RedisUtil;
import com.baiyi.caesar.domain.ErrorEnum;
import com.google.common.base.Joiner;
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

    @Resource
    private RedisUtil redisUtil;

    private String buildKey(String taskName) {
        return Joiner.on(":").join("caesar", "singleTask", taskName, "running");
    }

    @Pointcut(value = "@annotation(com.baiyi.caesar.common.annotation.SingleTask)")
    public void annotationPoint() {
    }

    @Before("annotationPoint()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
    }

    @Around("@annotation(singleTask)")
    public Object around(ProceedingJoinPoint joinPoint, SingleTask singleTask) throws Throwable {
        String key = buildKey(singleTask.name());
        try {
            if (redisUtil.get(key) == null) {
                redisUtil.set(key, 1, singleTask.lockSecond());
                joinPoint.proceed();
                redisUtil.del(key);
            } else {
                log.info("任务重复执行: taskKey = {} !",key);
                return new CommonRuntimeException(ErrorEnum. SINGLE_TASK_RUNNING);
            }
        } catch (Exception e) {
            redisUtil.del(key);
            return new Throwable();
        }
        return joinPoint;
    }
}
