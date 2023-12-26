package com.baiyi.opscloud.leo.handler.build.base;

import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2023/4/20 11:19
 * @Version 1.0
 */
@Slf4j
public class BuildStrategyFactory {

    private BuildStrategyFactory() {
    }

    static final  Map<String, Map<String, BaseBuildStrategy>> CONTEXT = new ConcurrentHashMap<>();

    public static BaseBuildStrategy getStrategy(String step, String type) {
        if (CONTEXT.containsKey(step)) {
            return CONTEXT.get(step).get(type);
        }
        throw new LeoBuildException("构建类型不正确: step={}, buildType={}", step, type);
    }

    public static void register(BaseBuildStrategy bean) {
        if (CONTEXT.containsKey(bean.getStep())) {
            CONTEXT.get(bean.getStep()).put(bean.getBuildType(), bean);
        } else {
            Map<String, BaseBuildStrategy> strategyMap = Maps.newHashMap();
            strategyMap.put(bean.getBuildType(), bean);
            CONTEXT.put(bean.getStep(), strategyMap);
        }
        log.debug("BuildStrategyFactory Registered: step={}, buildType={}", bean.getStep(), bean.getBuildType());
    }

}