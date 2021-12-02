package com.baiyi.opscloud.datasource.message.customer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/12/2 2:53 PM
 * @Version 1.0
 */
public class MessageCustomerFactory {

    private MessageCustomerFactory() {
    }

    static private final Map<String, IMessageCustomer> context = new ConcurrentHashMap<>();

    public static IMessageCustomer getConsumerByInstanceType(String instanceType) {
        return context.get(instanceType);
    }

    public static void register(IMessageCustomer bean) {
        context.put(bean.getInstanceType(), bean);
    }

}
