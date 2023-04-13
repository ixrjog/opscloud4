package com.baiyi.opscloud.datasource.business.account;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/8/11 1:40 下午
 * @Version 1.0
 */
public class AccountHandlerFactory {

    private AccountHandlerFactory() {
    }

    private static final Map<String, IAccount> CONTEXT = new ConcurrentHashMap<>();

    public static IAccount getByInstanceType(String instanceType) {
        return CONTEXT.get(instanceType);
    }

    public static void register(IAccount bean) {
        CONTEXT.put(bean.getInstanceType(), bean);
    }

}
