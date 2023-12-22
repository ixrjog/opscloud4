package com.baiyi.opscloud.sshserver.listeners.event;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2022/8/30 16:13
 * @Version 1.0
 */
@Slf4j
public class SshShellEventFactory {

    private static final Map<String, ISshShellEvent> CONTEXT = new ConcurrentHashMap<>();

    public static ISshShellEvent getByType(String key) {
        return CONTEXT.get(key);
    }

    public static void register(ISshShellEvent bean) {
        CONTEXT.put(bean.getEventType(), bean);
        log.debug("SshShellEventFactory Registered: eventType={}, beanName={}", bean.getEventType(), bean.getClass().getSimpleName());
    }

}