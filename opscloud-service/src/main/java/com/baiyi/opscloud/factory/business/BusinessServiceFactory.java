package com.baiyi.opscloud.factory.business;

import com.baiyi.opscloud.factory.business.base.IBusinessService;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/9/8 10:23 上午
 * @Version 1.0
 */
@Slf4j
public class BusinessServiceFactory {

    private BusinessServiceFactory() {
    }

    private static final Map<Integer, IBusinessService<?>> CONTEXT = new ConcurrentHashMap<>();

    public static IBusinessService<?> getIBusinessServiceByBusinessType(Integer businessType) {
        return CONTEXT.get(businessType);
    }

    public static void register(IBusinessService<?> bean) {
        CONTEXT.put(bean.getBusinessType(), bean);
        log.debug("BusinessServiceFactory Registered: beanName={}, businessType={}", bean.getClass().getSimpleName(), bean.getBusinessType());
    }

}