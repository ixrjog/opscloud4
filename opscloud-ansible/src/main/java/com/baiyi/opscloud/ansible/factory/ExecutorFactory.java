package com.baiyi.opscloud.ansible.factory;

import com.baiyi.opscloud.ansible.IAnsibleExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/4/17 8:54 上午
 * @Version 1.0
 */
public class ExecutorFactory {

    static Map<String, IAnsibleExecutor> context = new ConcurrentHashMap<>();

    public static IAnsibleExecutor getAnsibleExecutorByKey(String key) {
        return context.get(key);
    }

    public static void register(IAnsibleExecutor bean) {
        context.put(bean.getKey(), bean);
    }

    public static Map<String, IAnsibleExecutor> getAnsibleExecutorContainer() {
        return context;
    }

}
