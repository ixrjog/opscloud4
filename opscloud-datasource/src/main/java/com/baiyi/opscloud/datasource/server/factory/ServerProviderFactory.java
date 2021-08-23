package com.baiyi.opscloud.datasource.server.factory;

import com.baiyi.opscloud.datasource.server.IServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/8/19 11:11 上午
 * @Version 1.0
 */
public class ServerProviderFactory {

    private ServerProviderFactory() {
    }

    private static Map<String, IServer> context = new ConcurrentHashMap<>();

    public static IServer getIServerByInstanceType(String instanceType) {
        return context.get(instanceType);
    }

    public static void register(IServer bean) {
        context.put(bean.getInstanceType(), bean);
    }

    public static Map<String, IServer> getIServerContainer() {
        return context;
    }

}
