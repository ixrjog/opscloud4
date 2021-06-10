package com.baiyi.caesar.account.factory;

import com.baiyi.caesar.account.impl.BaseAccountHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class AccountHandlerFactory {

    static Map<String, BaseAccountHandler> context = new ConcurrentHashMap<>();

    public static BaseAccountHandler getAccountByKey(String key) {
        return context.get(key);
    }

    public static void register(BaseAccountHandler bean) {
        context.put(bean.getKey(), bean);
    }

    public static Map<String, BaseAccountHandler> getAccountContainer() {
        return context;
    }

}
