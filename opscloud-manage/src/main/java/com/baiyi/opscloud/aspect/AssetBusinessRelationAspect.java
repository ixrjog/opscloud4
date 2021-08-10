package com.baiyi.opscloud.aspect;

import com.baiyi.opscloud.domain.annotation.AssetBusinessRelation;
import com.baiyi.opscloud.service.tag.BusinessTagService;
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
 * @Date 2021/8/6 5:31 下午
 * @Version 1.0
 */
@Aspect
@Component
@Slf4j
public class AssetBusinessRelationAspect {

    @Resource
    private BusinessTagService businessTagService;

    @Pointcut(value = "@annotation(com.baiyi.opscloud.domain.annotation.AssetBusinessRelation)")
    public void annotationPoint() {
    }

    @Around("@annotation(assetBusinessRelation)")
    public Object around(ProceedingJoinPoint joinPoint, AssetBusinessRelation assetBusinessRelation) throws Throwable {
        Object result = joinPoint.proceed();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] params = methodSignature.getParameterNames();// 获取参数名称
        Object[] args = joinPoint.getArgs();// 获取参数值
        if (params != null && params.length != 0) {
            Integer businessId = Integer.valueOf(args[0].toString());
//            log.info("清除业务标签: businessType = {} , businessId = {}", tagClear.type().getType(), businessId);
//            businessTagService.deleteByBusinessTypeAndId(tagClear.type().getType(), businessId);
        }



        return result;
    }
}
