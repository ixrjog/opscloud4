package com.baiyi.opscloud.configuration.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static com.baiyi.opscloud.common.base.Global.ENV_PROD;

/**
 * @Author 修远
 * @Date 2023/1/17 11:13 AM
 * @Since 1.0
 */
public class EnvCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String env = context.getEnvironment().getActiveProfiles()[0];
        return ENV_PROD.equals(env);
    }

}
