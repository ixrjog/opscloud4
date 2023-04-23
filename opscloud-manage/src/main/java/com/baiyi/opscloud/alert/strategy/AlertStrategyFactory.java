package com.baiyi.opscloud.alert.strategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author 修远
 * @Date 2022/7/20 11:27 PM
 * @Since 1.0
 */
public class AlertStrategyFactory {

    private static final Map<String, IAlertStrategy> CONTEXT = new ConcurrentHashMap<>();

    public static IAlertStrategy getAlertActivity(String alertSeverity) {
        return CONTEXT.get(alertSeverity);
    }

    public static void register(IAlertStrategy bean) {
        CONTEXT.put(bean.getAlertSeverity(), bean);
    }

}
