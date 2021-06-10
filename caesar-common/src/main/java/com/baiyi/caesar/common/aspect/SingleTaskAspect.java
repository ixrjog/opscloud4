package com.baiyi.caesar.common.aspect;

import com.baiyi.caesar.common.redis.RedisUtil;
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
        return Joiner.on(":").join("caesar", "singleTask", taskName);
    }


    @Pointcut(value = "@annotation(com.baiyi.caesar.common.annotation.SingleTask)")
    public void annotationPoint() {
        System.err.print("SingleTask开始工作！");
    }

    @Before("annotationPoint()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        System.err.print("SingleTask开始工作！");
    }

    @Around(value = "annotationPoint()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.err.print("SingleTask开始工作！");
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        String key = buildKey(singleTask.name());
//
//        if (redisUtil.get(key) == null) {
//            System.err.print("SingleTask 任务锁不存在！");
//            redisUtil.set(key, 1, singleTask.lockSecond());
//            joinPoint.proceed();
//            redisUtil.del(key);
//        } else {
//            return new Throwable();
//        }


        return joinPoint;
    }
}
