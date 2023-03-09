package com.baiyi.opscloud.packer.leo;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.leo.LeoRuleVO;
import com.baiyi.opscloud.leo.domain.model.LeoRuleModel;
import com.baiyi.opscloud.leo.interceptor.rule.IRuleExpression;
import com.baiyi.opscloud.leo.interceptor.rule.LeoRuleExpressionFactory;
import com.baiyi.opscloud.packer.IWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/1/16 19:17
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class LeoRulePacker implements IWrapper<LeoRuleVO.Rule> {

    @Override
    public void wrap(LeoRuleVO.Rule rule, IExtend iExtend) {
        LeoRuleModel.RuleConfig ruleConfig = LeoRuleModel.load(rule);
        IRuleExpression ruleExpression = LeoRuleExpressionFactory.getExpressionByType(ruleConfig.getRule().getExpression().getType());
        if (ruleExpression == null) {
            return;
        }
        rule.setDisplayName(ruleExpression.toDisplayName(ruleConfig.getRule().getExpression()));
    }

}


