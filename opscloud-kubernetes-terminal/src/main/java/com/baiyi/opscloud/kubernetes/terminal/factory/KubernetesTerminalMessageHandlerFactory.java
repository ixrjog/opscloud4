package com.baiyi.opscloud.kubernetes.terminal.factory;

import com.baiyi.opscloud.sshcore.ITerminalMessageHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/7/15 10:02 上午
 * @Version 1.0
 */
public class KubernetesTerminalMessageHandlerFactory {

    static Map<String, ITerminalMessageHandler> context = new ConcurrentHashMap<>();

    public static ITerminalMessageHandler getHandlerByState(String state) {
        return context.get(state);
    }

    public static void register(ITerminalMessageHandler bean) {
        context.put(bean.getState(), bean);
    }

}