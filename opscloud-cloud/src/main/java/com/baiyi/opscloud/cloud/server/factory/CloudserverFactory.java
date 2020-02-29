package com.baiyi.opscloud.cloud.server.factory;

import com.baiyi.opscloud.cloud.server.ICloudserver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/1/9 3:42 下午
 * @Version 1.0
 */
public class CloudserverFactory {

    static Map<String, ICloudserver> context = new ConcurrentHashMap<>();

    public static ICloudserver getCloudserverByKey(String key) {
        return context.get(key);
    }

    public static void register(ICloudserver bean) {
        context.put(bean.getKey(), bean);
    }

    public static Map<String, ICloudserver> getCloudserverContainer() {
        return context;
    }


}
