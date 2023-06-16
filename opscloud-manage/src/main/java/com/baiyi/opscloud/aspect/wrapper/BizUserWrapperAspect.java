package com.baiyi.opscloud.aspect.wrapper;

import com.baiyi.opscloud.common.annotation.BizUserWrapper;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.business.IBusinessPermissionUser;
import com.baiyi.opscloud.packer.business.BusinessPermissionUserPacker;
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
 * @Date 2023/6/16 10:20
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class BizUserWrapperAspect {

    private final BusinessPermissionUserPacker businessPermissionUserPacker;

    @Pointcut(value = "@annotation(com.baiyi.opscloud.common.annotation.BizUserWrapper)")
    public void annotationPoint() {
    }

    @Around("@annotation(bizUserWrapper)")
    public Object around(ProceedingJoinPoint joinPoint, BizUserWrapper bizUserWrapper) throws OCException {
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new OCException(e.getMessage());
        }
        boolean extend = bizUserWrapper.extend();
        IBusinessPermissionUser target = null;
        if (bizUserWrapper.wrapResult()) {
            target = (IBusinessPermissionUser) result;
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 获取参数名称
        String[] params = methodSignature.getParameterNames();
        // 获取参数值
        Object[] args = joinPoint.getArgs();
        if (params != null && params.length != 0) {
            for (Object arg : args) {
                if (!extend) {
                    if (arg instanceof IExtend) {
                        extend = ((IExtend) arg).getExtend() != null && ((IExtend) arg).getExtend();
                        continue;
                    }
                }
                if (target == null) {
                    if (arg instanceof IBusinessPermissionUser) {
                        target = (IBusinessPermissionUser) arg;
                    }
                }
            }
        }
        if (extend && target != null) {
            wrap(target);
        }
        return result;
    }

    public void wrap(IBusinessPermissionUser iBusinessPermissionUser) {
        businessPermissionUserPacker.wrap(iBusinessPermissionUser);
    }
}
