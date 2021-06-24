package com.baiyi.caesar.datasource.factory;

import com.baiyi.caesar.datasource.provider.auth.BaseAuthProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/6/23 9:38 上午
 * @Version 1.0
 */
public class AuthProviderFactory {

    private AuthProviderFactory() {
    }

    static private Map<String, BaseAuthProvider> context = new ConcurrentHashMap<>();

    public static BaseAuthProvider getProvider(String instanceType) {
        return context.get(instanceType);
    }

    public static void register(BaseAuthProvider bean) {
        context.put(bean.getInstanceType(), bean);
    }
}
