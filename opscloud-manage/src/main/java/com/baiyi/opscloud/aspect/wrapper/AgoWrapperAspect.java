package com.baiyi.opscloud.aspect.wrapper;

import com.baiyi.opscloud.common.annotation.AgoWrapper;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.util.time.AgoUtil;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.base.ShowTime;
import com.baiyi.opscloud.service.sys.EnvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/2/23 11:08 AM
 * @Version 1.0
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AgoWrapperAspect {

    private final EnvService envService;

    @Pointcut(value = "@annotation(com.baiyi.opscloud.common.annotation.AgoWrapper)")
    public void annotationPoint() {
    }

    @Around("@annotation(agoWrapper)")
    public Object around(ProceedingJoinPoint joinPoint, AgoWrapper agoWrapper) throws CommonRuntimeException {
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new CommonRuntimeException(e.getMessage());
        }
        boolean extend = agoWrapper.extend();
        ShowTime.IAgo targetAgo = null;
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
                if (targetAgo == null) {
                    if (arg instanceof ShowTime.IAgo) {
                        targetAgo = (ShowTime.IAgo) arg;
                    }
                }
            }
        }
        if (extend && targetAgo != null) {
            wrap(targetAgo);
        }
        return result;
    }

    public void wrap(ShowTime.IAgo iAgo) {
        if (iAgo.getAgoTime() == null) return;
        iAgo.setAgo(AgoUtil.format(iAgo.getAgoTime()));
    }

}
