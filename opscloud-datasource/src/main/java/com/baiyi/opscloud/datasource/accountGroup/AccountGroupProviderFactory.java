package com.baiyi.opscloud.datasource.accountGroup;

import com.baiyi.opscloud.datasource.account.IAccount;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/8/24 10:44 上午
 * @Version 1.0
 */
public class AccountGroupProviderFactory {

    private AccountGroupProviderFactory() {
    }

    private static Map<String, IAccountGroup> context = new ConcurrentHashMap<>();

    public static IAccountGroup getIAccountByInstanceType(String instanceType) {
        return context.get(instanceType);
    }

    public static void register(IAccountGroup bean) {
        context.put(bean.getInstanceType(), bean);
    }

    public static Map<String, IAccountGroup> getIAccountContainer() {
        return context;
    }
}
