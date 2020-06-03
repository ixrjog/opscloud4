package com.baiyi.opscloud.factory.change;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/5/27 4:16 下午
 * @Version 1.0
 */
public class ServerChangeFactory {

    static Map<String, IServerChange> context = new ConcurrentHashMap<>();

    public static IServerChange getServerChangeByKey(String key) {
        return context.get(key);
    }

    public static void register(IServerChange bean) {
        context.put(bean.getKey(), bean);
    }

    public static Map<String, IServerChange> getServerChangeContainer() {
        return context;
    }
}
