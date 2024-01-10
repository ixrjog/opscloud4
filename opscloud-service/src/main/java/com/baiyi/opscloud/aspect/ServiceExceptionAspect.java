package com.baiyi.opscloud.aspect;

import com.baiyi.opscloud.common.annotation.ServiceExceptionCatch;
import com.baiyi.opscloud.common.exception.common.OCException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

/**
 * @Author 修远
 * @Date 2022/7/25 3:44 PM
 * @Since 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ServiceExceptionAspect {

    @Pointcut(value = "@annotation(com.baiyi.opscloud.common.annotation.ServiceExceptionCatch)")
    public void annotationPoint() {
    }

    @Around("@annotation(serviceExceptionCatch)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, ServiceExceptionCatch serviceExceptionCatch) throws Throwable {
        try {
            return proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            if (throwable instanceof DuplicateKeyException) {
                throw new OCException(serviceExceptionCatch.message());
            }
            throw throwable;
        }
    }

}