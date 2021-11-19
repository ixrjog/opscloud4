package com.baiyi.opscloud.datasource.account;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/8/11 1:40 下午
 * @Version 1.0
 */
public class AccountProviderFactory {

    private AccountProviderFactory() {
    }

    private static final Map<String, IAccount> context = new ConcurrentHashMap<>();

    public static IAccount getIAccountByInstanceType(String instanceType) {
        return context.get(instanceType);
    }

    public static void register(IAccount bean) {
        context.put(bean.getInstanceType(), bean);
    }

    public static Map<String, IAccount> getIAccountContainer() {
        return context;
    }
}
