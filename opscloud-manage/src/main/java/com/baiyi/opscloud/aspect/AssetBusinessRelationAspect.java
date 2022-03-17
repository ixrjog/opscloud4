package com.baiyi.opscloud.aspect;

import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.domain.annotation.AssetBusinessRelation;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
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
 * 业务对象绑定资产(User,UserGroup,Server,ServerGroup)
 *
 * @Author baiyi
 * @Date 2021/8/6 5:31 下午
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AssetBusinessRelationAspect {

    private final BusinessAssetRelationFacade businessAssetRelationFacade;

    @Pointcut(value = "@annotation(com.baiyi.opscloud.domain.annotation.AssetBusinessRelation)")
    public void annotationPoint() {
    }

    @Around("@annotation(assetBusinessRelation)")
    public Object around(ProceedingJoinPoint joinPoint, AssetBusinessRelation assetBusinessRelation) throws CommonRuntimeException {
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new CommonRuntimeException(e.getMessage());
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] params = methodSignature.getParameterNames();// 获取参数名称
        Object[] args = joinPoint.getArgs();// 获取参数值
        if (params != null && params.length != 0) {
            Object obj = args[0];
            if (obj instanceof BusinessAssetRelationVO.IBusinessAssetRelation) {
                BusinessAssetRelationVO.IBusinessAssetRelation bar = (BusinessAssetRelationVO.IBusinessAssetRelation) obj;
                bindRelation(bar);
            }
        }
        return result;
    }

    /**
     * 业务对象与资产绑定（无需传入assetId）
     *
     * @param bar
     */
    private void bindRelation(BusinessAssetRelationVO.IBusinessAssetRelation bar) {
        log.info("业务对象绑定资产: businessType = {} , businessId = {} , assetId = {}", bar.getBusinessType(), bar.getBusinessId(), bar.getAssetId());
        businessAssetRelationFacade.bindAsset(bar);
    }

}
