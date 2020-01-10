package com.baiyi.opscloud.account.factory;

import com.baiyi.opscloud.account.Account;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class AccountFactory {

    static Map<String, Account> context = new ConcurrentHashMap<>();

    public static Account getAccountByKey(String key) {
        return context.get(key);
    }

    public static void register(Account bean) {
        context.put(bean.getKey(), bean);
    }

    public static Map<String, Account> getAccountContainer() {
        return context;
    }

}
