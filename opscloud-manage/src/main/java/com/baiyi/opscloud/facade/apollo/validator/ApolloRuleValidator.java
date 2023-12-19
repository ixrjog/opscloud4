package com.baiyi.opscloud.facade.apollo.validator;

import com.baiyi.opscloud.domain.base.SimpleBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.LeoRule;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.baiyi.opscloud.facade.tag.SimpleTagFacade;
import com.baiyi.opscloud.leo.domain.model.LeoRuleModel;
import com.baiyi.opscloud.leo.exception.LeoInterceptorException;
import com.baiyi.opscloud.leo.interceptor.rule.IRuleExpression;
import com.baiyi.opscloud.leo.interceptor.rule.LeoRuleExpressionFactory;
import com.baiyi.opscloud.service.application.ApplicationService;
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
public class ApolloRuleValidator{

    private final ApplicationService applicationService;

    private final SimpleTagFacade simpleTagFacade;

    /**
     * 验证
     *
     * @param event
     * @param rule
     */
    public void verify(ApolloParam.ReleaseEvent event, LeoRule rule) {
        LeoRuleModel.RuleConfig ruleConfig = LeoRuleModel.load(rule);
        // 标签校验
        boolean hitTagRule = checkRuleWithTag(event.getAppId(), ruleConfig);
        // 校验环境
        boolean hitEnvRule = checkRuleWithEnv(event.getEnv(), ruleConfig);
        // 时间表达式校验
        boolean hitExpressionRule = checkRuleWithExpression(ruleConfig);

        if (hitTagRule && hitEnvRule && hitExpressionRule) {
            throw new LeoInterceptorException("拦截器命中规则阻止任务执行: ruleId={}", rule.getId());
        }
    }

    private boolean checkRuleWithTag(String appName, LeoRuleModel.RuleConfig ruleConfig) {
        Application application = applicationService.getByName(appName);

        if (application == null) {
            return false;
        }

        // 标签校验
        List<String> tags = Optional.ofNullable(ruleConfig)
                .map(LeoRuleModel.RuleConfig::getRule)
                .map(LeoRuleModel.Rule::getTags)
                .orElse(Collections.emptyList());
        if (CollectionUtils.isEmpty(tags)) {
            return false;
        }
        SimpleBusiness sb = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.APPLICATION.getType())
                .businessId(application.getId())
                .build();
        List<TagVO.Tag> appTags = simpleTagFacade.queryTagByBusiness(sb);
        return tags.stream().anyMatch(e -> anyMatchTag(e, appTags));
    }

    private boolean anyMatchTag(String tag, List<TagVO.Tag> appTags) {
        return appTags.stream().anyMatch(e -> e.getTagKey().equalsIgnoreCase(tag));
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