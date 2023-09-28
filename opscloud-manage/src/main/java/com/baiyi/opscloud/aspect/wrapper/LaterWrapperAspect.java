package com.baiyi.opscloud.aspect.wrapper;

import com.baiyi.opscloud.common.annotation.LaterWrapper;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.time.LaterUtil;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.base.ReadableTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 注入later字段
 * @Author baiyi
 * @Date 2022/2/23 11:33 AM
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
public class LaterWrapperAspect {

    @Pointcut(value = "@annotation(com.baiyi.opscloud.common.annotation.LaterWrapper)")
    public void annotationPoint() {
    }

    @Around("@annotation(laterWrapper)")
    public Object around(ProceedingJoinPoint joinPoint, LaterWrapper laterWrapper) throws OCException {
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new OCException(e.getMessage());
        }
        boolean extend = laterWrapper.extend();
        ReadableTime.ILater laterTarget = null;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 获取参数名称
        String[] params = methodSignature.getParameterNames();
        // 获取参数值
        Object[] args = joinPoint.getArgs();
        if (params != null && params.length != 0) {
            for (Object arg : args) {
                if (!extend) {
                    if (arg instanceof IExtend) {
                        extend = ((IExtend) arg).getExtend();
                        continue;
                    }
                }
                if (laterTarget == null) {
                    if (arg instanceof ReadableTime.ILater) {
                        laterTarget = (ReadableTime.ILater) arg;
                    }
                }
            }
        }
        if (extend && laterTarget != null) {
            LaterUtil.wrap(laterTarget);
        }
        return result;
    }

}
