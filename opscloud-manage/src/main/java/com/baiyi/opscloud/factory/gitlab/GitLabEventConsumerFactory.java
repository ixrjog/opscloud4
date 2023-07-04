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
public class GitLabEventConsumerFactory {

    private static final Map<String, IGitLabEventConsumer> CONTEXT = new ConcurrentHashMap<>();

    public static IGitLabEventConsumer getByEventName(String eventName) {
        return CONTEXT.get(eventName);
    }

    public static void register(IGitLabEventConsumer bean) {
        for (String eventName : bean.getEventNames()) {
            CONTEXT.put(eventName, bean);
            log.debug("GitLabEventConsumeFactory Registered: eventName={}, beanName={}", eventName, bean.getClass().getSimpleName());
        }
    }

}
