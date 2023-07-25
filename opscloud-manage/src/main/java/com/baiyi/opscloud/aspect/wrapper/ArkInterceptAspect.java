package com.baiyi.opscloud.aspect.wrapper;

import com.baiyi.opscloud.common.annotation.ArkIntercept;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/2/21 13:39
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@Order(value = -100)
public class ArkInterceptAspect {

    @Pointcut(value = "@annotation(com.baiyi.opscloud.common.annotation.ArkIntercept)")
    public void annotationPoint() {
    }

    @Around("@annotation(arkIntercept)")
    public Object around(ProceedingJoinPoint joinPoint, ArkIntercept arkIntercept) throws Throwable {
        return joinPoint.proceed();
    }

}
