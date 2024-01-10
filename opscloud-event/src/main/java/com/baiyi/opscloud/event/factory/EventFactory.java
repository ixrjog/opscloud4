package com.baiyi.opscloud.event.factory;

import com.baiyi.opscloud.event.IEventHandler;
import com.baiyi.opscloud.event.enums.EventTypeEnum;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/10/9 3:07 下午
 * @Version 1.0
 */
public class EventFactory {

    private EventFactory() {
    }

    private static final Map<String, IEventHandler> CONTEXT = new ConcurrentHashMap<>();

    public static IEventHandler getByEventType(EventTypeEnum eventType) {
        return CONTEXT.get(eventType.name());
    }

    /**
     * 注册
     *
     * @param bean
     */
    public static void register(IEventHandler bean) {
        CONTEXT.put(bean.getEventType().name(), bean);
    }

}