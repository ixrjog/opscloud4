package com.baiyi.opscloud.cloud.db.factory;

import com.baiyi.opscloud.cloud.db.ICloudDB;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/1/9 3:42 下午
 * @Version 1.0
 */
public class CloudDBFactory {

    static Map<String, ICloudDB> context = new ConcurrentHashMap<>();

    public static ICloudDB getCloudDBByKey(String key) {
        return context.get(key);
    }

    public static void register(ICloudDB bean) {
        context.put(bean.getKey(), bean);
    }

    public static Map<String, ICloudDB> getCloudDBContainer() {
        return context;
    }


}
