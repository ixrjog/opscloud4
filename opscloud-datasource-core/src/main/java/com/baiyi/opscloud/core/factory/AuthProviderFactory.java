package com.baiyi.opscloud.core.factory;

import com.baiyi.opscloud.core.provider.auth.BaseAuthProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/6/23 9:38 上午
 * @Version 1.0
 */
@Slf4j
public class AuthProviderFactory {

    private AuthProviderFactory() {
    }

    private static final Map<String, BaseAuthProvider> CONTEXT = new ConcurrentHashMap<>();

    public static BaseAuthProvider getProvider(String instanceType) {
        return CONTEXT.get(instanceType);
    }

    public static void register(BaseAuthProvider bean) {
        CONTEXT.put(bean.getInstanceType(), bean);
        log.debug("AuthProviderFactory Registered: beanName={}, instanceType={}", bean.getClass().getSimpleName(), bean.getInstanceType());
    }

}