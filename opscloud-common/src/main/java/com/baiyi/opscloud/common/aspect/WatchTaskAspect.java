package com.baiyi.opscloud.common.aspect;

import com.baiyi.opscloud.common.annotation.WatchTask;
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

/**
 * @Author baiyi
 * @Date 2023/1/29 14:03
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Order(-1)
public class WatchTaskAspect {

    @Pointcut(value = "@annotation(com.baiyi.opscloud.common.annotation.WatchTask)")
    public void annotationPoint() {
    }

    @Before("annotationPoint()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
    }

    @Around("@annotation(watchTask)")
    public Object around(ProceedingJoinPoint joinPoint, WatchTask watchTask) throws Throwable {
        final String taskName = watchTask.name();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start(taskName);
        log.info("Task {} start", taskName);
        Object result = joinPoint.proceed();
        stopWatch.stop();
        log.info("Task {} end, runtime={}/s", taskName, stopWatch.getTotalTimeSeconds());
        return result;
    }

}