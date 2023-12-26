package com.baiyi.opscloud.core.provider.aspect;

import com.baiyi.opscloud.core.provider.annotation.ChildProvider;
import com.baiyi.opscloud.core.provider.annotation.EnablePullChild;
import com.baiyi.opscloud.core.provider.base.asset.SimpleAssetProvider;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.Map;

/**
 * @Author 修远
 * @Date 2021/7/6 5:40 下午
 * @Since 1.0
 */
@SuppressWarnings("rawtypes")
@Aspect
@Component
public class EnablePullChildAspect {

    @Resource
    private ApplicationContext applicationContext;

    @Around(value = "@annotation(enablePullChild)")
    public Object doAround(ProceedingJoinPoint pjp, EnablePullChild enablePullChild) throws Throwable {
        Object result = pjp.proceed();
        Object requestObj = pjp.getArgs()[0];
        Map<String, Object> map = applicationContext.getBeansWithAnnotation(ChildProvider.class);
        map.forEach((k, v) -> {
            if (AopUtils.getTargetClass(v).getAnnotation(ChildProvider.class).parentType().equals(enablePullChild.type())) {
                SimpleAssetProvider provider = (SimpleAssetProvider) v;
                provider.pullAsset((Integer) requestObj);
            }
        });
        return result;
    }

}