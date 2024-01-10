package com.baiyi.opscloud.workorder.processor.factory;

import com.baiyi.opscloud.workorder.processor.ITicketProcessor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2022/1/6 6:50 PM
 * @Version 1.0
 */
@SuppressWarnings("rawtypes")
@Slf4j
public class WorkOrderTicketProcessorFactory {

    private static final Map<String, ITicketProcessor> CONTEXT = new ConcurrentHashMap<>();

    public static ITicketProcessor getByKey(String key) {
        return CONTEXT.get(key);
    }

    public static void register(ITicketProcessor bean) {
        CONTEXT.put(bean.getKey(), bean);
        log.debug("WorkOrderTicketProcessorFactory Registered: key={}, beanName={}", bean.getKey(), bean.getClass().getSimpleName());
    }

}