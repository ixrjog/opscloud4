package com.baiyi.opscloud.datasource.server.factory;

import com.baiyi.opscloud.datasource.server.IServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/22 3:33 下午
 * @Since 1.0
 */
public class ServerFactory {

    private ServerFactory() {
    }

    private static Map<String, IServer> context = new ConcurrentHashMap<>();

    public static IServer getIServerByAssetType(String assetType) {
        return context.get(assetType);
    }

    public static void register(IServer bean) {
        context.put(bean.getAssetType(), bean);
    }

    public static Map<String, IServer> getIServerContainer() {
        return context;
    }
}
