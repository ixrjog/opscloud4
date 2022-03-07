package com.baiyi.opscloud.factory.gitlab;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/5/6 10:37 上午
 * @Version 1.0
 */
@Slf4j
public class GitlabEventConsumerFactory {

    private static final Map<String, IGitlabEventConsumer> context = new ConcurrentHashMap<>();

    public static IGitlabEventConsumer getByEventName(String eventName) {
        return context.get(eventName);
    }

    public static void register(IGitlabEventConsumer bean) {
        for (String eventName : bean.getEventNames()) {
            context.put(eventName, bean);
            log.info("GitlabEventConsumeFactory注册: eventName = {} , beanName = {}  ", eventName, bean.getClass().getSimpleName());
        }
    }

}
