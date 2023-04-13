package com.baiyi.opscloud.datasource.business.server.factory;

import com.baiyi.opscloud.datasource.business.server.IServerGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/8/24 1:31 下午
 * @Version 1.0
 */
public class ServerGroupHandlerFactory {

    private ServerGroupHandlerFactory() {
    }

    private static final Map<String, IServerGroup> CONTEXT = new ConcurrentHashMap<>();

    public static IServerGroup getByInstanceType(String instanceType) {
        return CONTEXT.get(instanceType);
    }

    public static void register(IServerGroup bean) {
        CONTEXT.put(bean.getInstanceType(), bean);
    }

}
