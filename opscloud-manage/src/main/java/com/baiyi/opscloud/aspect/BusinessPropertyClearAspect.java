package com.baiyi.opscloud.aspect;

import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.domain.annotation.BusinessPropertyClear;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.service.business.BusinessPropertyService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 清理业务对象属性
 *
 * @Author baiyi
 * @Date 2021/8/25 3:27 下午
 * @Version 1.0
 */
@Aspect
@Component
@Slf4j
public class BusinessPropertyClearAspect {

    @Resource
    private BusinessPropertyService businessPropertyService;

    @Pointcut(value = "@annotation(com.baiyi.opscloud.domain.annotation.BusinessPropertyClear)")
    public void annotationPoint() {
    }

    @Around("@annotation(businessPropertyClear)")
    public Object around(ProceedingJoinPoint joinPoint, BusinessPropertyClear businessPropertyClear) throws CommonRuntimeException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] params = methodSignature.getParameterNames();// 获取参数名称
        Object[] args = joinPoint.getArgs();// 获取参数值
        if (params != null && params.length != 0) {
            Integer businessId = Integer.valueOf(args[0].toString());
            if (businessPropertyClear.value() == BusinessTypeEnum.COMMON) {
                // 通过@BusinessType 获取业务类型
                if (joinPoint.getTarget().getClass().isAnnotationPresent(BusinessType.class)) {
                    BusinessType businessType = joinPoint.getTarget().getClass().getAnnotation(BusinessType.class);
                    doClear(businessType.value().getType(), businessId);
                }
            } else {
                doClear(businessPropertyClear.value().getType(), businessId);
            }
        }
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw new CommonRuntimeException(e.getMessage());
        }
    }

    private void doClear(Integer businessType, Integer businessId) {
        log.info("清除业务属性: businessType = {} , businessId = {}", businessType, businessId);
        businessPropertyService.deleteByBusinessTypeAndId(businessType, businessId);
    }
}
