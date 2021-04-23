package com.baiyi.opscloud.cloud.server.factory;

import com.baiyi.opscloud.cloud.server.ICloudServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/1/9 3:42 下午
 * @Version 1.0
 */
public class CloudServerFactory {

    static Map<String, ICloudServer> context = new ConcurrentHashMap<>();

    public static ICloudServer getCloudServerByKey(String key) {
        return context.get(key);
    }

    public static void register(ICloudServer bean) {
        context.put(bean.getKey(), bean);
    }

    public static Map<String, ICloudServer> getCloudServerContainer() {
        return context;
    }

}
