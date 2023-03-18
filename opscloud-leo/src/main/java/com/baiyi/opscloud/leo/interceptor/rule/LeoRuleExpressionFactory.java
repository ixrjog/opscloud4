package com.baiyi.opscloud.leo.interceptor.rule;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2023/1/10 10:24
 * @Version 1.0
 */
@Slf4j
public class LeoRuleExpressionFactory {

    private LeoRuleExpressionFactory() {
    }

    private static final Map<String, IRuleExpression> context = new ConcurrentHashMap<>();

    public static IRuleExpression getExpressionByType(String type) {
        return context.get(type);
    }

    public static void register(IRuleExpression bean) {
        context.put(bean.getType(), bean);
        log.debug("LeoRuleExpressionFactory Registered: beanName={}, type={}", bean.getClass().getSimpleName(), bean.getType());
    }

}
