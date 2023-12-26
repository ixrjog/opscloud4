package com.baiyi.opscloud.leo.handler.deploy.base;

import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2022/12/14 09:35
 * @Version 1.0
 */
@Slf4j
public class DeployStrategyFactory {

    private DeployStrategyFactory() {
    }

    static final Map<String, Map<String, BaseDeployStrategy>> CONTEXT = new ConcurrentHashMap<>();

    public static BaseDeployStrategy getStrategy(String step, String type) {
        if (CONTEXT.containsKey(step)) {
            return CONTEXT.get(step).get(type);
        }
        throw new LeoDeployException("部署类型不正确: step={}, deployType={}", step, type);
    }

    public static void register(BaseDeployStrategy bean) {
        if (CONTEXT.containsKey(bean.getStep())) {
            CONTEXT.get(bean.getStep()).put(bean.getDeployType(), bean);
        } else {
            Map<String, BaseDeployStrategy> strategyMap = Maps.newHashMap();
            strategyMap.put(bean.getDeployType(), bean);
            CONTEXT.put(bean.getStep(), strategyMap);
        }
        log.debug("DeployStrategyFactory Registered: step={}, deployType={}", bean.getStep(), bean.getDeployType());
    }

}