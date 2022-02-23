package com.baiyi.opscloud.aspect.wrapper;

import com.baiyi.opscloud.common.annotation.LaterWrapper;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.util.time.LaterUtil;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.base.ShowTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/2/23 11:33 AM
 * @Version 1.0
 */
@Aspect
@Component
@Slf4j
public class LaterWrapperAspect {

    @Pointcut(value = "@annotation(com.baiyi.opscloud.common.annotation.LaterWrapper)")
    public void annotationPoint() {
    }

    @Around("@annotation(laterWrapper)")
    public Object around(ProceedingJoinPoint joinPoint, LaterWrapper laterWrapper) throws CommonRuntimeException {
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new CommonRuntimeException(e.getMessage());
        }
        boolean extend = laterWrapper.extend();
        ShowTime.ILater targetLater = null;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] params = methodSignature.getParameterNames();// 获取参数名称
        Object[] args = joinPoint.getArgs();// 获取参数值
        if (params != null && params.length != 0) {
            for (Object arg : args) {
                if (!extend) {
                    if (arg instanceof IExtend) {
                        extend = ((IExtend) arg).getExtend();
                        continue;
                    }
                }
                if (targetLater == null) {
                    if (arg instanceof ShowTime.ILater) {
                        targetLater = (ShowTime.ILater) arg;
                    }
                }
            }
        }
        if (extend && targetLater != null) {
            wrap(targetLater);
        }
        return result;
    }

    public void wrap(ShowTime.ILater iLater) {
        if (iLater.getExpiredTime() == null) return;
        iLater.setLater(LaterUtil.format(iLater.getExpiredTime()));
    }

}
