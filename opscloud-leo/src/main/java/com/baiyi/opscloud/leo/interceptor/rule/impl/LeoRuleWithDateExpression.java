package com.baiyi.opscloud.leo.interceptor.rule.impl;

import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.leo.constants.RuleExpressionConstants;
import com.baiyi.opscloud.leo.domain.model.LeoRuleModel;
import com.baiyi.opscloud.leo.interceptor.rule.BaseLeoRuleExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 按具体时间范围来匹配
 *
 * @Author baiyi
 * @Date 2023/1/10 10:32
 * @Version 1.0
 */
@Slf4j
@Component
public class LeoRuleWithDateExpression extends BaseLeoRuleExpression {

    public String getType() {
        return RuleExpressionConstants.DATE.name();
    }

    private static final String DISPLAY_NAME = "按时间封网(开始时间: {}, 结束时间: {})";

    public boolean parse(LeoRuleModel.Expression expression) {
        LeoRuleModel.DateExpression expr = LeoRuleModel.DateExpression
                .build(expression);
        return expr.parse();
    }

    /**
     * @param expression
     * @return 封网开始时间: 2023-01-16 00:00:00, 封网结束时间 2023-01-29 09:00:00
     */
    public String toDisplayName(LeoRuleModel.Expression expression) {
        return StringFormatter.arrayFormat(DISPLAY_NAME, expression.getBegin(), expression.getEnd());
    }

}