package com.baiyi.opscloud.leo.build;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2022/11/11 14:58
 * @Version 1.0
 */
@Slf4j
public class BuildFactory {

    private BuildFactory() {
    }

    private static final Map<String, IBuildHandler> context = new ConcurrentHashMap<>();

    public static IBuildHandler getBuildHandler(String buildType) {
        return context.get(buildType);
    }

    public static void register(IBuildHandler bean) {
        context.put(bean.getBuildType(), bean);
        log.info("BuildFactory Registered: buildType={}", bean.getBuildType());
    }

}
