package com.baiyi.opscloud.terminal.audit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/7/23 2:54 下午
 * @Version 1.0
 */
public class TerminalAuditProcessFactory {

    static Map<String, ITerminalAuditProcess> context = new ConcurrentHashMap<>();

    public static ITerminalAuditProcess getProcessByKey(String key) {
        return context.get(key);
    }

    public static void register(ITerminalAuditProcess bean) {
        context.put(bean.getState(), bean);
    }
}
