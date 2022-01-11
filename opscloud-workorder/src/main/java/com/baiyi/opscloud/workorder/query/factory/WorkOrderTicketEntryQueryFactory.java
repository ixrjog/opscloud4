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
@Slf4j
public class WorkOrderTicketEntryQueryFactory {

    private static final Map<String, ITicketEntryQuery> context = new ConcurrentHashMap<>();

    public static ITicketEntryQuery getByKey(String key) {
        return context.get(key);
    }

    public static void register(ITicketEntryQuery bean) {
        context.put(bean.getKey(), bean);
        log.info("WorkOrderTicketEntryQueryFactory注册: key = {} , beanName = {}  ", bean.getKey(), bean.getClass().getSimpleName());
    }
}
