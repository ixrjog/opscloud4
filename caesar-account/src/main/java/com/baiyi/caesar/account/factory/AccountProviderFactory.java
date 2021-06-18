package com.baiyi.caesar.account.factory;

import com.baiyi.caesar.account.IAccountProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class AccountProviderFactory {

    private AccountProviderFactory() {
    }

    static Map<String, IAccountProvider> context = new ConcurrentHashMap<>();

    public static IAccountProvider getAccountByKey(String key) {
        return context.get(key);
    }

    public static void register(IAccountProvider bean) {
        context.put(bean.getKey(), bean);
    }

}
