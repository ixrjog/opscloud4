package com.baiyi.opscloud.facade.apollo;

import com.baiyi.opscloud.domain.generator.opscloud.LeoRule;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;
import com.baiyi.opscloud.leo.domain.model.LeoRuleModel;
import com.baiyi.opscloud.leo.exception.LeoInterceptorException;
import com.baiyi.opscloud.leo.interceptor.rule.IRuleExpression;
import com.baiyi.opscloud.leo.interceptor.rule.LeoRuleExpressionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Apollo 规则校验器
 *
 * @Author baiyi
 * @Date 2023/5/31 15:37
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApolloRuleValidator {

    /**
     * 验证
     *
     * @param event
     * @param rule
     */
    public void verify(ApolloParam.ReleaseEvent event, LeoRule rule) {
        LeoRuleModel.RuleConfig ruleConfig = LeoRuleModel.load(rule);
        // 校验环境
        boolean hitEnvRule = checkRuleWithEnv(event.getEnv(), ruleConfig);
        // 时间表达式校验
        boolean hitExpressionRule = checkRuleWithExpression(ruleConfig);

        if (hitEnvRule && hitExpressionRule) {
            throw new LeoInterceptorException("拦截器命中规则阻止任务执行: ruleId={}", rule.getId());
        }
    }

    /**
     * 校验环境
     *
     * @param apolloEnv
     * @param ruleConfig
     * @return
     */
    private boolean checkRuleWithEnv(String apolloEnv, LeoRuleModel.RuleConfig ruleConfig) {
        List<String> envs = Optional.ofNullable(ruleConfig)
                .map(LeoRuleModel.RuleConfig::getRule)
                .map(LeoRuleModel.Rule::getEnvs)
                .orElse(Collections.emptyList());
        if (CollectionUtils.isEmpty(envs)) {
            return false;
        }
        return envs.stream().anyMatch(e -> e.equalsIgnoreCase(apolloEnv));
    }

    private boolean checkRuleWithExpression(LeoRuleModel.RuleConfig ruleConfig) {
        LeoRuleModel.Expression expression = Optional.ofNullable(ruleConfig)
                .map(LeoRuleModel.RuleConfig::getRule)
                .map(LeoRuleModel.Rule::getExpression)
                .orElseThrow(() -> new LeoInterceptorException("拦截器规则表达式未填写！"));
        log.debug("校验规则: expression={}", expression);
        IRuleExpression bean = LeoRuleExpressionFactory.getExpressionByType(expression.getType());
        if (bean == null) {
            throw new LeoInterceptorException("规则表达式类型不正确: type={}", expression.getType());
        }
        return bean.parse(expression);
    }

}
