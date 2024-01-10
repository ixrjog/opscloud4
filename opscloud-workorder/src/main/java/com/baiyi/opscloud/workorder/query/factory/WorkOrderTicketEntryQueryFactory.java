package com.baiyi.opscloud.workorder.query.factory;

import com.baiyi.opscloud.workorder.query.ITicketEntryQuery;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2022/1/11 3:47 PM
 * @Version 1.0
 */
@SuppressWarnings("rawtypes")
@Slf4j
public class WorkOrderTicketEntryQueryFactory {

    private static final Map<String, ITicketEntryQuery> CONTEXT = new ConcurrentHashMap<>();

    public static ITicketEntryQuery getByKey(String key) {
        return CONTEXT.get(key);
    }

    public static void register(ITicketEntryQuery bean) {
        CONTEXT.put(bean.getKey(), bean);
        log.debug("WorkOrderTicketEntryQueryFactory Registered: key={}, beanName={}", bean.getKey(), bean.getClass().getSimpleName());
    }

}