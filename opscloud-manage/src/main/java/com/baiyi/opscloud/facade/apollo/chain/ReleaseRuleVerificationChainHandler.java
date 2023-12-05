package com.baiyi.opscloud.facade.apollo.chain;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.common.datasource.ApolloConfig;
import com.baiyi.opscloud.domain.generator.opscloud.LeoRule;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;
import com.baiyi.opscloud.facade.apollo.handler.BaseApolloReleaseChainHandler;
import com.baiyi.opscloud.facade.apollo.validator.ApolloRuleValidator;
import com.baiyi.opscloud.leo.domain.model.LeoRuleModel;
import com.baiyi.opscloud.leo.exception.LeoInterceptorException;
import com.baiyi.opscloud.service.leo.LeoRuleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;

/**
 * @Author baiyi
 * @Date 2023/11/13 09:12
 * @Version 1.0
 */
@Slf4j
@Component
public class ReleaseRuleVerificationChainHandler extends BaseApolloReleaseChainHandler {

    @Resource
    private ApolloRuleValidator apolloRuleValidator;

    @Resource
    private LeoRuleService leoRuleService;

    @Override
    protected HttpResult<Boolean> handle(ApolloParam.ReleaseEvent releaseEvent, ApolloConfig apolloConfig) {
        // 规则校验
        try {
            verifyRule(releaseEvent);
            return notify(apolloConfig, releaseEvent);
        } catch (LeoInterceptorException e) {
            return HttpResult.<Boolean>builder()
                    .success(false)
                    .code(SC_BAD_REQUEST)
                    .msg(e.getMessage())
                    .build();
        }
    }

    /**
     * 校验
     */
    public void verifyRule(ApolloParam.ReleaseEvent releaseEvent) {
        final String env = releaseEvent.getEnv().toLowerCase();
        List<LeoRule> rules = leoRuleService.queryAll();
        rules.forEach(rule -> {
            LeoRuleModel.RuleConfig ruleConfig = LeoRuleModel.load(rule);
            List<String> envs = Optional.ofNullable(ruleConfig)
                    .map(LeoRuleModel.RuleConfig::getRule)
                    .map(LeoRuleModel.Rule::getEnvs)
                    .orElse(Collections.emptyList());
            if (!CollectionUtils.isEmpty(envs)) {
                if (envs.stream().anyMatch(e -> e.equalsIgnoreCase(env))) {
                    try {
                        apolloRuleValidator.verify(releaseEvent, rule);
                    } catch (LeoInterceptorException e) {
                        throw new LeoInterceptorException("当前规则禁止执行: {}！", rule.getName());
                    }
                }
            }
        });
    }

}