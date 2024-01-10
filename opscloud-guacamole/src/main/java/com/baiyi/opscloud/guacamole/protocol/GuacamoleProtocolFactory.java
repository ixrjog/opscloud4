package com.baiyi.opscloud.guacamole.protocol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/7/9 1:41 下午
 * @Version 1.0
 */
public class GuacamoleProtocolFactory {

    private GuacamoleProtocolFactory() {
    }

    static private final Map<String, IGuacamoleProtocol> context = new ConcurrentHashMap<>();

    public static IGuacamoleProtocol getProtocol(String protocol) {
        return context.get(protocol);
    }

    public static void register(IGuacamoleProtocol bean) {
        context.put(bean.getProtocol(), bean);
    }

}