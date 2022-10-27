package com.baiyi.opscloud.terminal.audit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/7/23 2:54 下午
 * @Version 1.0
 */
public class TerminalAuditHandlerFactory {

    private static final Map<String, ITerminalAuditHandler> context = new ConcurrentHashMap<>();

    public static ITerminalAuditHandler getHandlerByKey(String key) {
        return context.get(key);
    }

    public static void register(ITerminalAuditHandler bean) {
        context.put(bean.getState(), bean);
    }
}
