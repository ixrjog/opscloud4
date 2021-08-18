package com.baiyi.opscloud.aspect;

import com.baiyi.opscloud.common.annotation.EventPublisher;
import com.baiyi.opscloud.event.NoticeEvent;
import com.baiyi.opscloud.event.SimpleEvent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/17 6:07 下午
 * @Version 1.0
 */
@Aspect
@Component
public class EventPublisherAspect {

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Pointcut(value = "@annotation(com.baiyi.opscloud.common.annotation.EventPublisher)")
    public void annotationPoint() {
    }

    @Around("@annotation(eventPublisher)")
    public Object around(ProceedingJoinPoint joinPoint, EventPublisher eventPublisher) throws Throwable {
        Object result = joinPoint.proceed();
        // 后处理事件
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] params = methodSignature.getParameterNames();// 获取参数名称
        Object[] args = joinPoint.getArgs();// 获取参数值
        if (params != null && params.length != 0) {
            Object body = args[0];
            SimpleEvent simpleEvent = SimpleEvent.builder()
                    .eventType(eventPublisher.eventType().name())
                    .action(eventPublisher.eventAction().name())
                    .body(body)
                    .build();
            // 发送事件
            applicationEventPublisher.publishEvent(new NoticeEvent(simpleEvent));
        }
        return result;
    }
}
