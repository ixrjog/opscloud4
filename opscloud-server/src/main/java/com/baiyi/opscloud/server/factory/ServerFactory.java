package com.baiyi.opscloud.server.factory;

import com.baiyi.opscloud.server.IServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/4/1 3:39 下午
 * @Version 1.0
 */
public class ServerFactory {

    static Map<String, IServer> context = new ConcurrentHashMap<>();

    public static IServer getIServerByKey(String key) {
        return context.get(key);
    }

    public static void register(IServer bean) {
        context.put(bean.getKey(), bean);
    }

    public static Map<String, IServer> getIServerContainer() {
        return context;
    }
}
