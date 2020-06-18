package com.baiyi.opscloud.factory.change.consumer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/5/29 11:36 上午
 * @Version 1.0
 */
public class ServerChangeConsumerFactory {

    static Map<String, IServerChangeConsumer> context = new ConcurrentHashMap<>();

    public static IServerChangeConsumer getServerChangeConsumerByKey(String key) {
        return context.get(key);
    }

    public static void register(IServerChangeConsumer bean) {
        context.put(bean.getKey(), bean);
    }

    public static Map<String, IServerChangeConsumer> getServerChangeConsumerContainer() {
        return context;
    }
}
