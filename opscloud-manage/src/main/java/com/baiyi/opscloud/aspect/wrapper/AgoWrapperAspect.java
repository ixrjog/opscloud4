package com.baiyi.opscloud.aspect.wrapper;

import com.baiyi.opscloud.common.annotation.AgoWrapper;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.time.AgoUtil;
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
 * 注入ago字段
 *
 * @Author baiyi
 * @Date 2022/2/23 11:08 AM
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
public class AgoWrapperAspect {

    @Pointcut(value = "@annotation(com.baiyi.opscloud.common.annotation.AgoWrapper)")
    public void annotationPoint() {
    }

    @Around("@annotation(agoWrapper)")
    public Object around(ProceedingJoinPoint joinPoint, AgoWrapper agoWrapper) throws OCException {
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new OCException(e.getMessage());
        }
        boolean extend = agoWrapper.extend();
        ReadableTime.IAgo agoTarget = null;
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
                if (agoTarget == null) {
                    if (arg instanceof ReadableTime.IAgo) {
                        agoTarget = (ReadableTime.IAgo) arg;
                    }
                }
            }
        }
        if (extend && agoTarget != null) {
            AgoUtil.wrap(agoTarget);
        }
        return result;
    }

}
