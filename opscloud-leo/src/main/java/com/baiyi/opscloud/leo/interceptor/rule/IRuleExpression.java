package com.baiyi.opscloud.leo.interceptor.rule;

import com.baiyi.opscloud.leo.domain.model.LeoRuleModel;

/**
 * @Author baiyi
 * @Date 2023/1/10 15:44
 * @Version 1.0
 */
public interface IRuleExpression {

    String getType();

    boolean parse(LeoRuleModel.Expression expression);

    String toDisplayName(LeoRuleModel.Expression expression);

}
