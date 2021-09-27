package com.baiyi.opscloud.ansible.play;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/9/27 9:42 上午
 * @Version 1.0
 */
public class ServerTaskPlayFactory {

    private ServerTaskPlayFactory() {
    }

    static Map<String, ITaskPlayProcess> context = new ConcurrentHashMap<>();

    public static ITaskPlayProcess getProcessByKey(String key) {
        return context.get(key);
    }

    public static void register(ITaskPlayProcess bean) {
        context.put(bean.getState(), bean);
    }
}
