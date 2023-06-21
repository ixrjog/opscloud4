package com.baiyi.opscloud.leo.dict.factory;

import com.baiyi.opscloud.leo.dict.IBuildDictGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2023/4/25 11:31
 * @Version 1.0
 */
@Slf4j
public class BuildDictGeneratorFactory {

    private BuildDictGeneratorFactory() {
    }

    private static final Map<String, IBuildDictGenerator> CONTEXT = new ConcurrentHashMap<>();

    public static IBuildDictGenerator getGenerator(String buildType) {
        if (CONTEXT.containsKey(buildType)) {
            return CONTEXT.get(buildType);
        }
        return null;
    }

    public static void register(IBuildDictGenerator bean) {
        CONTEXT.put(bean.getBuildType(), bean);
        log.debug("BuildDictGeneratorFactory Registered: beanName={}, buildType={}", bean.getClass().getSimpleName(), bean.getBuildType());
    }

}