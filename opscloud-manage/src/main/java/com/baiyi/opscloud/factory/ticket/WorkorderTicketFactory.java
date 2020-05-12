package com.baiyi.opscloud.factory.ticket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/4/27 2:57 下午
 * @Version 1.0
 */
public class WorkorderTicketFactory {

    static Map<String, ITicketHandler> context = new ConcurrentHashMap<>();

    public static ITicketHandler getTicketHandlerByKey(String key) {
        return context.get(key);
    }

    public static void register(ITicketHandler bean) {
        context.put(bean.getKey(), bean);
    }

    public static Map<String, ITicketHandler> getTicketHandlerContainer() {
        return context;
    }
}
