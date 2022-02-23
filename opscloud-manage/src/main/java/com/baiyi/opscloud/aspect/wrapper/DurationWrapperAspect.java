package com.baiyi.opscloud.aspect.wrapper;

import com.baiyi.opscloud.common.annotation.DurationWrapper;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.base.ShowTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @Author baiyi
 * @Date 2022/2/23 1:10 PM
 * @Version 1.0
 */
@Aspect
@Component
@Slf4j
public class DurationWrapperAspect {

    @Pointcut(value = "@annotation(com.baiyi.opscloud.common.annotation.DurationWrapper)")
    public void annotationPoint() {
    }

    @Around("@annotation(durationWrapper)")
    public Object around(ProceedingJoinPoint joinPoint, DurationWrapper durationWrapper) throws CommonRuntimeException {
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new CommonRuntimeException(e.getMessage());
        }
        boolean extend = durationWrapper.extend();
        ShowTime.IDuration targetDuration = null;
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
                if (targetDuration == null) {
                    if (arg instanceof ShowTime.IDuration) {
                        targetDuration = (ShowTime.IDuration) arg;
                    }
                }
            }
        }
        if (extend && targetDuration != null) {
            wrap(targetDuration);
        }
        return result;
    }

    private void wrap(ShowTime.IDuration iDuration) {
        if (iDuration.getStartTime() == null || iDuration.getEndTime() == null) return;
        long diffTime = iDuration.getEndTime().getTime() - iDuration.getStartTime().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
        iDuration.setDuration(formatter.format(diffTime));
    }

}
