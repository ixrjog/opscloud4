package com.baiyi.opscloud.leo.dict.factory;

import com.baiyi.opscloud.leo.dict.IBuildDictProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2023/4/25 11:31
 * @Version 1.0
 */
@Slf4j
public class BuildDictFactory {

    private BuildDictFactory() {
    }

    private static final Map<String, IBuildDictProvider> CONTEXT = new ConcurrentHashMap<>();

    public static IBuildDictProvider getProvider(String buildType) {
        if (CONTEXT.containsKey(buildType)) {
            return CONTEXT.get(buildType);
        }
        return null;
    }

    public static void register(IBuildDictProvider bean) {
        CONTEXT.put(bean.getBuildType(), bean);
        log.debug("BuildDictFactory Registered: beanName={}, buildType={}", bean.getClass().getSimpleName(), bean.getBuildType());
    }

}