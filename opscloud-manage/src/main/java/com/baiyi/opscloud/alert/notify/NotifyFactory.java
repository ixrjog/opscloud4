package com.baiyi.opscloud.alert.notify;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author 修远
 * @Date 2022/7/21 12:11 AM
 * @Since 1.0
 */
public class NotifyFactory {

    private static final Map<String, INotify> context = new ConcurrentHashMap<>();

    public static INotify getNotifyActivity(String key) {
        return context.get(key);
    }

    public static void register(INotify bean) {
        context.put(bean.getKey(), bean);
    }

}
