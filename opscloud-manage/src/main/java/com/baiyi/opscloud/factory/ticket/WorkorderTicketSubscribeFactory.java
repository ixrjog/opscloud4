package com.baiyi.opscloud.factory.ticket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/5/6 1:29 下午
 * @Version 1.0
 */
public class WorkorderTicketSubscribeFactory {

    static Map<String, ITicketSubscribe> context = new ConcurrentHashMap<>();

    public static ITicketSubscribe getTicketSubscribeByKey(String key) {
        return context.get(key);
    }

    public static void register(ITicketSubscribe bean) {
        context.put(bean.getKey(), bean);
    }
}
