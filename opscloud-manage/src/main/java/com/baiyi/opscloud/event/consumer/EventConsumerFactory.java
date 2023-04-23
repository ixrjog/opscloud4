package com.baiyi.opscloud.event.consumer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/8/17 6:55 下午
 * @Version 1.0
 */
public class EventConsumerFactory {

    private EventConsumerFactory() {
    }

    static private final Map<String, IEventConsumer> CONTEXT = new ConcurrentHashMap<>();

    public static IEventConsumer getConsumer(String eventType) {
        return CONTEXT.get(eventType);
    }

    public static void register(IEventConsumer bean) {
        CONTEXT.put(bean.getEventType(), bean);
    }

}
