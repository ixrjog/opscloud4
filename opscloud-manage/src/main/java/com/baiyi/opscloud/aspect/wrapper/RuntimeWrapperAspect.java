package com.baiyi.opscloud.aspect.wrapper;

import com.baiyi.opscloud.common.annotation.RuntimeWrapper;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.base.ReadableTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 注入runtime字段
 *
 * @Author baiyi
 * @Date 2022/11/30 22:04
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
public class RuntimeWrapperAspect {

    @Pointcut(value = "@annotation(com.baiyi.opscloud.common.annotation.RuntimeWrapper)")
    public void annotationPoint() {
    }

    @Around("@annotation(runtimeWrapper)")
    public Object around(ProceedingJoinPoint joinPoint, RuntimeWrapper runtimeWrapper) throws OCException {
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new OCException(e.getMessage());
        }
        boolean extend = runtimeWrapper.extend();
        ReadableTime.IRuntime runtimeTarget = null;
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
                if (runtimeTarget == null) {
                    if (arg instanceof ReadableTime.IRuntime) {
                        runtimeTarget = (ReadableTime.IRuntime) arg;
                    }
                }
            }
        }
        if (extend && runtimeTarget != null) {
            wrap(runtimeTarget);
        }
        return result;
    }

    private void wrap(ReadableTime.IRuntime iRuntime) {
        if (StringUtils.isNotBlank(iRuntime.getRuntime())) {
            return;
        }
        if (iRuntime.getStartTime() == null) {
            return;
        }
        long runtime;
        if (iRuntime.getEndTime() != null) {
            runtime = iRuntime.getEndTime().getTime() - iRuntime.getStartTime().getTime();
        } else {
            runtime = System.currentTimeMillis() - iRuntime.getStartTime().getTime();
        }
        iRuntime.setRuntime(NewTimeUtil.toRuntime(runtime));
    }

}
