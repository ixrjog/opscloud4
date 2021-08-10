package com.baiyi.opscloud.aspect;

import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.domain.annotation.AssetBusinessUnbindRelation;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.facade.datasource.BusinessAssetRelationFacade;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 解除业务对象与资产的绑定关系
 *
 * @Author baiyi
 * @Date 2021/8/10 11:40 上午
 * @Version 1.0
 */
@Aspect
@Component
@Slf4j
public class AssetBusinessUnbindRelationAspect {

    @Resource
    private BusinessAssetRelationFacade businessAssetRelationFacade;

    @Pointcut(value = "@annotation(com.baiyi.opscloud.domain.annotation.AssetBusinessUnbindRelation)")
    public void annotationPoint() {
    }

    @Around("@annotation(assetBusinessUnbindRelation)")
    public Object around(ProceedingJoinPoint joinPoint, AssetBusinessUnbindRelation assetBusinessUnbindRelation) throws CommonRuntimeException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] params = methodSignature.getParameterNames();// 获取参数名称
        Object[] args = joinPoint.getArgs();// 获取参数值
        if (params != null && params.length != 0) {
            Integer businessId = Integer.valueOf(args[0].toString());
            unbindAsset(assetBusinessUnbindRelation.type(),businessId);
        }
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw new CommonRuntimeException(e.getMessage());
        }
    }

    private void unbindAsset(BusinessTypeEnum businessTypeEnum, Integer businessId){
        log.info("解除业务对象与资产的绑定关系: businessType = {} , businessId = {}", businessTypeEnum.getType(), businessId);
        businessAssetRelationFacade.unbindAsset(businessTypeEnum.getType(), businessId);
    }

}
