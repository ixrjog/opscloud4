package com.baiyi.opscloud.factory.xterm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/5/11 10:50 上午
 * @Version 1.0
 */
public class XTermProcessFactory {

    static Map<String, IXTermProcess> context = new ConcurrentHashMap<>();

    public static IXTermProcess getIXTermProcessByKey(String key) {
        return context.get(key);
    }

    public static void register(IXTermProcess bean) {
        context.put(bean.getKey(), bean);
    }
}
