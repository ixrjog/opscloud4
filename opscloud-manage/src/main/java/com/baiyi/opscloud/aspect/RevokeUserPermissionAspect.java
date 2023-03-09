package com.baiyi.opscloud.aspect;

import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.annotation.RevokeUserPermission;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.facade.user.UserPermissionFacade;
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
 * @Date 2021/8/18 2:28 下午
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RevokeUserPermissionAspect {

    private final UserPermissionFacade userPermissionFacade;

    @Pointcut(value = "@annotation(com.baiyi.opscloud.domain.annotation.RevokeUserPermission)")
    public void annotationPoint() {
    }

    @Around("@annotation(revokeUserPermission)")
    public Object around(ProceedingJoinPoint joinPoint, RevokeUserPermission revokeUserPermission) throws OCException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 获取参数名称
        String[] params = methodSignature.getParameterNames();
        // 获取参数值
        Object[] args = joinPoint.getArgs();
        if (params != null && params.length != 0) {
            Integer businessId = Integer.valueOf(args[0].toString());
            if (revokeUserPermission.value() == BusinessTypeEnum.COMMON) {
                // 通过@BusinessType 获取业务类型
                if (joinPoint.getTarget().getClass().isAnnotationPresent(BusinessType.class)) {
                    BusinessType businessType = joinPoint.getTarget().getClass().getAnnotation(BusinessType.class);
                    revokeHandle(businessType.value().getType(), businessId);
                }
            } else {
                revokeHandle(revokeUserPermission.value().getType(), businessId);
            }
        }
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw new OCException(e.getMessage());
        }
    }

    private void revokeHandle(Integer businessType, Integer businessId) {
        log.info("撤销当前业务对象的所有用户授权: businessType={}, businessId={}", businessType, businessId);
        if (BusinessTypeEnum.USER.getType() == businessType) {
            // 撤销用户的所有授权
            userPermissionFacade.revokeByUserId(businessId);
        } else {
            // 撤销业务的所有授权
            userPermissionFacade.revokeUserPermissionByBusiness(businessType, businessId);
        }
    }

}
