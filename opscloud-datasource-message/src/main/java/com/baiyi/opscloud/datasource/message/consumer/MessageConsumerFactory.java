package com.baiyi.opscloud.datasource.message.consumer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/12/2 2:53 PM
 * @Version 1.0
 */
public class MessageConsumerFactory {

    private MessageConsumerFactory() {
    }

    static private final Map<String, IMessageConsumer> context = new ConcurrentHashMap<>();

    public static IMessageConsumer getConsumerByInstanceType(String instanceType) {
        return context.get(instanceType);
    }

    public static void register(IMessageConsumer bean) {
        context.put(bean.getInstanceType(), bean);
    }

}