package com.baiyi.opscloud.aspect;

import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.domain.annotation.AssetBusinessUnbindRelation;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.base.SimpleBusiness;
import com.baiyi.opscloud.facade.datasource.BusinessAssetRelationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 解除业务对象与资产的绑定关系
 *
 * @Author baiyi
 * @Date 2021/8/10 11:40 上午
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AssetBusinessUnbindRelationAspect {

    private final BusinessAssetRelationFacade businessAssetRelationFacade;

    @Pointcut(value = "@annotation(com.baiyi.opscloud.domain.annotation.AssetBusinessUnbindRelation)")
    public void annotationPoint() {
    }

    @Around("@annotation(assetBusinessUnbindRelation)")
    public Object around(ProceedingJoinPoint joinPoint, AssetBusinessUnbindRelation assetBusinessUnbindRelation) throws OCException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 获取参数名称
        String[] params = methodSignature.getParameterNames();
        // 获取参数值
        Object[] args = joinPoint.getArgs();
        if (params != null && params.length != 0) {
            SimpleBusiness simpleBusiness = SimpleBusiness.builder()
                    .businessType(assetBusinessUnbindRelation.type().getType())
                    .businessId(Integer.valueOf(args[0].toString()))
                    .build();

            unbindAsset(simpleBusiness);
        }
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw new OCException(e.getMessage());
        }
    }

    private void unbindAsset(BaseBusiness.IBusiness iBusiness) {
        log.info("解除业务对象与资产的绑定关系: businessType={}, businessId={}", iBusiness.getBusinessType(), iBusiness.getBusinessId());
        businessAssetRelationFacade.unbindAsset(iBusiness);
    }

}
