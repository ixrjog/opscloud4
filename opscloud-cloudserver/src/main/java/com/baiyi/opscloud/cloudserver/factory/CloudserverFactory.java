package com.baiyi.opscloud.cloudserver.factory;

import com.baiyi.opscloud.cloudserver.Cloudserver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/1/9 3:42 下午
 * @Version 1.0
 */
public class CloudserverFactory {

    static Map<String, Cloudserver> context = new ConcurrentHashMap<>();

    public static Cloudserver getCloudserverByKey(String key) {
        return context.get(key);
    }

    public static void register(Cloudserver bean) {
        context.put(bean.getKey(), bean);
    }

    public static Map<String, Cloudserver> getCloudserverContainer() {
        return context;
    }


}
