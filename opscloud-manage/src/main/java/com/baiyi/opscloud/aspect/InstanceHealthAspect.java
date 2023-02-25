package com.baiyi.opscloud.aspect;

import com.baiyi.opscloud.domain.annotation.InstanceHealth;
import com.baiyi.opscloud.facade.sys.InstanceFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.baiyi.opscloud.common.base.Global.ENV_PROD;

/**
 * 实例健康检查AOP
 *
 * @Author baiyi
 * @Date 2021/9/3 2:25 下午
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Order(-65535)
public class InstanceHealthAspect {

    @Value("${spring.profiles.active}")
    private String env;

    private final InstanceFacade instanceFacade;

    @Pointcut(value = "@annotation(com.baiyi.opscloud.domain.annotation.InstanceHealth)")
    public void annotationPoint() {
    }

    @Around("@annotation(instanceHealth)")
    public Object around(ProceedingJoinPoint joinPoint, InstanceHealth instanceHealth) throws Throwable {
        if (ENV_PROD.equals(env)) {
            if (instanceFacade.isHealth()) {
                log.debug("Opscloud instance health examination: passed !");
                return joinPoint.proceed();
            } else {
                log.debug("Opscloud instance health examination: down !");
                return joinPoint;
            }
        }
        return joinPoint.proceed();
    }

}
