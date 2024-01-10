package com.baiyi.opscloud.leo.interceptor.rule.impl;

import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.leo.constants.RuleExpressionConstants;
import com.baiyi.opscloud.leo.domain.model.LeoRuleModel;
import com.baiyi.opscloud.leo.interceptor.rule.BaseLeoRuleExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 按每天时间范围来匹配
 *
 * @Author baiyi
 * @Date 2023/3/16 10:08
 * @Version 1.0
 */
@Slf4j
@Component
public class LeoRuleWithDailyExpression extends BaseLeoRuleExpression {

    public String getType() {
        return RuleExpressionConstants.DAILY.name();
    }

    private static final String DISPLAY_NAME = "每天封网(开始时间: {}, 结束时间: {})";

    public boolean parse(LeoRuleModel.Expression expression) {
        LeoRuleModel.DailyExpression expr = LeoRuleModel.DailyExpression
                .build(expression);
        return expr.parse();
    }

    /**
     * @param expression
     * @return 封网开始时间: 11:40:00, 封网结束时间 13:20:00
     */
    public String toDisplayName(LeoRuleModel.Expression expression) {
        return StringFormatter.arrayFormat(DISPLAY_NAME, expression.getBegin(), expression.getEnd());
    }

}