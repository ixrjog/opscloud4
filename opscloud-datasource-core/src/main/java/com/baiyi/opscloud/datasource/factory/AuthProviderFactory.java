package com.baiyi.opscloud.datasource.factory;

import com.baiyi.opscloud.datasource.provider.auth.BaseAuthProvider;
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

    static private Map<String, BaseAuthProvider> context = new ConcurrentHashMap<>();

    public static BaseAuthProvider getProvider(String instanceType) {
        return context.get(instanceType);
    }

    public static void register(BaseAuthProvider bean) {
        log.info("AuthProviderFactory注册: beanName = {} , instanceType = {}", bean.getClass().getSimpleName(), bean.getInstanceType());
        context.put(bean.getInstanceType(), bean);
    }
}
