package com.baiyi.opscloud.terminal.factory;

import com.baiyi.opscloud.sshcore.ITerminalMessageHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/5/11 10:50 上午
 * @Version 1.0
 */
public class ServerTerminalMessageHandlerFactory {

    static Map<String, ITerminalMessageHandler> context = new ConcurrentHashMap<>();

    public static ITerminalMessageHandler getHandlerByState(String state) {
        return context.get(state);
    }

    public static void register(ITerminalMessageHandler bean) {
        context.put(bean.getState(), bean);
    }
}
