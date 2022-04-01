package com.baiyi.opscloud.datasource.business.serverGroup.factory;

import com.baiyi.opscloud.datasource.business.serverGroup.IServerGroup;

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

    private static final Map<String, IServerGroup> context = new ConcurrentHashMap<>();

    public static IServerGroup getIServerGroupByInstanceType(String instanceType) {
        return context.get(instanceType);
    }

    public static void register(IServerGroup bean) {
        context.put(bean.getInstanceType(), bean);
    }

    public static Map<String, IServerGroup> getIServerGroupContainer() {
        return context;
    }
}
