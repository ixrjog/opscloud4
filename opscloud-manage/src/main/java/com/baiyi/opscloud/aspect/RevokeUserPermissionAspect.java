package com.baiyi.opscloud.aspect;

import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.domain.annotation.RevokeUserPermission;
import com.baiyi.opscloud.facade.user.UserPermissionFacade;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/18 2:28 下午
 * @Version 1.0
 */
@Aspect
@Component
@Slf4j
public class RevokeUserPermissionAspect {

    @Resource
    private UserPermissionFacade userPermissionFacade;

    @Pointcut(value = "@annotation(com.baiyi.opscloud.domain.annotation.RevokeUserPermission)")
    public void annotationPoint() {
    }

    @Around("@annotation(revokeUserPermission)")
    public Object around(ProceedingJoinPoint joinPoint, RevokeUserPermission revokeUserPermission) throws CommonRuntimeException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] params = methodSignature.getParameterNames();// 获取参数名称
        Object[] args = joinPoint.getArgs();// 获取参数值
        if (params != null && params.length != 0) {
            int businessId = Integer.parseInt(args[0].toString());
            log.info("清除当前业务对象的所有用户授权: businessType = {} , businessId = {}", revokeUserPermission.type().getType(), businessId);
            userPermissionFacade.revokeUserPermissionByBusiness(revokeUserPermission.type().getType(), businessId);
        }
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw new CommonRuntimeException(e.getMessage());
        }

    }

}
