package com.baiyi.opscloud.kubernetes.terminal.factory;

import com.baiyi.opscloud.sshcore.ITerminalProcess;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/7/15 10:02 上午
 * @Version 1.0
 */
public class KubernetesTerminalProcessFactory {

    static Map<String, ITerminalProcess> context = new ConcurrentHashMap<>();

    public static ITerminalProcess getProcessByKey(String key) {
        return context.get(key);
    }

    public static void register(ITerminalProcess bean) {
        context.put(bean.getState(), bean);
    }
}
