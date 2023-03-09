package com.baiyi.opscloud.aspect;

import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.annotation.TagClear;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 清理业务对象标签
 *
 * @Author baiyi
 * @Date 2021/5/25 8:57 上午
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class TagClearAspect {

    private final BusinessTagService businessTagService;

    @Pointcut(value = "@annotation(com.baiyi.opscloud.domain.annotation.TagClear)")
    public void annotationPoint() {
    }

    @Around("@annotation(tagClear)")
    public Object around(ProceedingJoinPoint joinPoint, TagClear tagClear) throws OCException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 获取参数名称
        String[] params = methodSignature.getParameterNames();
        // 获取参数值
        Object[] args = joinPoint.getArgs();
        if (params != null && params.length != 0) {
            Integer businessId = Integer.valueOf(args[0].toString());
            if (tagClear.value() == BusinessTypeEnum.COMMON) {
                // 通过@BusinessType 获取业务类型
                if (joinPoint.getTarget().getClass().isAnnotationPresent(BusinessType.class)) {
                    BusinessType businessType = joinPoint.getTarget().getClass().getAnnotation(BusinessType.class);
                    doClear(businessType.value().getType(), businessId);
                }
            } else {
                doClear(tagClear.value().getType(), businessId);
            }
        }
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw new OCException(e.getMessage());
        }
    }

    private void doClear(Integer businessType, Integer businessId) {
        log.info("清除业务标签: businessType={}, businessId={}", businessType, businessId);
        businessTagService.deleteByBusinessTypeAndId(businessType, businessId);
    }

}
