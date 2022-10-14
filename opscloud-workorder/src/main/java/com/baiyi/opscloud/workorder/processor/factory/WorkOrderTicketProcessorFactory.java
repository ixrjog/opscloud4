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
@Slf4j
public class WorkOrderTicketProcessorFactory {

    private static final Map<String, ITicketProcessor> context = new ConcurrentHashMap<>();

    public static ITicketProcessor getByKey(String key) {
        return context.get(key);
    }

    public static void register(ITicketProcessor bean) {
        context.put(bean.getKey(), bean);
        log.info("WorkOrderTicketProcessorFactory Registered: key={}, beanName={}", bean.getKey(), bean.getClass().getSimpleName());
    }

}
