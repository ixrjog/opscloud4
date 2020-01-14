package com.baiyi.opscloud.account.factory;

import com.baiyi.opscloud.account.IAccount;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class AccountFactory {

    static Map<String, IAccount> context = new ConcurrentHashMap<>();

    public static IAccount getAccountByKey(String key) {
        return context.get(key);
    }

    public static void register(IAccount bean) {
        context.put(bean.getKey(), bean);
    }

    public static Map<String, IAccount> getAccountContainer() {
        return context;
    }

}
