package com.baiyi.opscloud.aspect;

import com.baiyi.opscloud.domain.annotation.InstanceHealth;
import com.baiyi.opscloud.facade.sys.InstanceFacade;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
public class InstanceHealthAspect implements Ordered {

    @Value("${spring.profiles.active}")
    private String env;

    @Resource
    private InstanceFacade instanceFacade;

    @Pointcut(value = "@annotation(com.baiyi.opscloud.domain.annotation.InstanceHealth)")
    public void annotationPoint() {
    }

    @Around("@annotation(instanceHealth)")
    public Object around(ProceedingJoinPoint joinPoint, InstanceHealth instanceHealth) throws Throwable {
        if (ENV_PROD.equals(env)) {
            if (instanceFacade.isHealth()) {
                log.info("InstanceHealthAspect: passed !");
                return joinPoint.proceed();
            } else {
                // throw new CommonRuntimeException("InstanceHealthAspect: 当前实例不可用！");
                log.error("InstanceHealthAspect: 当前实例不可用 !");
                return joinPoint;
            }
        }
        return joinPoint.proceed();
    }

    @Override
    public int getOrder() {
        return 1;
    }

}
