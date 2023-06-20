package com.baiyi.opscloud.leo.interceptor.rule;

import com.baiyi.opscloud.domain.base.SimpleBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.generator.opscloud.LeoRule;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.baiyi.opscloud.facade.tag.SimpleTagFacade;
import com.baiyi.opscloud.leo.domain.model.LeoRuleModel;
import com.baiyi.opscloud.leo.exception.LeoInterceptorException;
import com.baiyi.opscloud.service.sys.EnvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/12/29 19:40
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RuleValidator {

    private final EnvService envService;

    private final SimpleTagFacade simpleTagFacade;

    public void verify(LeoJob leoJob, LeoRule rule) {
        LeoRuleModel.RuleConfig ruleConfig = LeoRuleModel.load(rule);
        // 标签校验
        boolean hitTagRule = checkRuleWithTag(leoJob, ruleConfig);
        // 环境校验
        boolean hitEnvRule = checkRuleWithEnv(leoJob, ruleConfig);
        // 时间表达式校验
        boolean hitExpressionRule = checkRuleWithExpression(ruleConfig);
        // 返回结果
        if (hitTagRule && hitEnvRule && hitExpressionRule) {
            throw new LeoInterceptorException("拦截器命中规则阻止任务执行: ruleId={}", rule.getId());
        }
    }

    public boolean checkRuleWithExpression(LeoRuleModel.RuleConfig ruleConfig) {
        LeoRuleModel.Expression expression = Optional.ofNullable(ruleConfig)
                .map(LeoRuleModel.RuleConfig::getRule)
                .map(LeoRuleModel.Rule::getExpression)
                .orElseThrow(() -> new LeoInterceptorException("拦截器规则表达式未填写！"));
        log.info("校验规则: expression={}", expression);
        IRuleExpression bean = LeoRuleExpressionFactory.getExpressionByType(expression.getType());
        if (bean == null) {
            throw new LeoInterceptorException("规则表达式类型不正确: type={}", expression.getType());
        }
        return bean.parse(expression);
    }

    private boolean checkRuleWithTag(LeoJob leoJob, LeoRuleModel.RuleConfig ruleConfig) {
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
                .businessId(leoJob.getApplicationId())
                .build();
        List<TagVO.Tag> appTags = simpleTagFacade.queryTagByBusiness(sb);
        return tags.stream().anyMatch(e -> anyMatchTag(e, appTags));
    }

    private boolean anyMatchTag(String tag, List<TagVO.Tag> appTags) {
        return appTags.stream().anyMatch(e -> e.getTagKey().equalsIgnoreCase(tag));
    }

    private boolean checkRuleWithEnv(LeoJob leoJob, LeoRuleModel.RuleConfig ruleConfig) {
        List<String> envs = Optional.ofNullable(ruleConfig)
                .map(LeoRuleModel.RuleConfig::getRule)
                .map(LeoRuleModel.Rule::getEnvs)
                .orElse(Collections.emptyList());
        if (CollectionUtils.isEmpty(envs)) {
            return false;
        }
        Env env = envService.getByEnvType(leoJob.getEnvType());
        return envs.stream().anyMatch(e -> e.equalsIgnoreCase(env.getEnvName()));
    }

}