package com.baiyi.opscloud.datasource.business.server.factory;

import com.baiyi.opscloud.datasource.business.server.IServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/8/19 11:11 上午
 * @Version 1.0
 */
public class ServerHandlerFactory {

    private ServerHandlerFactory() {
    }

    private static final Map<String, IServer> CONTEXT = new ConcurrentHashMap<>();

    public static IServer getByInstanceType(String instanceType) {
        return CONTEXT.get(instanceType);
    }

    public static void register(IServer bean) {
        CONTEXT.put(bean.getInstanceType(), bean);
    }


}
