package com.baiyi.opscloud.datasource.ansible.play;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/9/27 9:42 上午
 * @Version 1.0
 */
@Slf4j
public class ServerTaskPlayFactory {

    private ServerTaskPlayFactory() {
    }

    static Map<String, ITaskPlayProcessor> context = new ConcurrentHashMap<>();

    public static ITaskPlayProcessor getProcessByKey(String key) {
        return context.get(key);
    }

    public static void register(ITaskPlayProcessor bean) {
        context.put(bean.getState(), bean);
        log.debug("TemplateFactory Registered: state={}", bean.getState());
    }

}
