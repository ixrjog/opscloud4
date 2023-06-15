package com.baiyi.opscloud.facade.apollo;

import com.baiyi.opscloud.domain.generator.opscloud.LeoRule;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;

/**
 * Apollo 规则校验器
 *
 * @Author baiyi
 * @Date 2023/5/31 15:37
 * @Version 1.0
 */
public interface ApolloRuleValidator {

    void verify(ApolloParam.ReleaseEvent event, LeoRule rule);

}
