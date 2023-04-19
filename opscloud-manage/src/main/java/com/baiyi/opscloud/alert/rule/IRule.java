package com.baiyi.opscloud.alert.rule;

import com.baiyi.opscloud.domain.alert.AlertContext;
import com.baiyi.opscloud.domain.alert.AlertRuleMatchExpression;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;

/**
 * @Author 修远
 * @Date 2022/7/21 2:38 PM
 * @Since 1.0
 */
public interface IRule {

    Boolean evaluate(DsAssetVO.Asset asset, AlertRuleMatchExpression matchExpression);

    Boolean failureDeadline(DsAssetVO.Asset asset, AlertRuleMatchExpression matchExpression);

    Boolean silence(DsAssetVO.Asset asset, AlertRuleMatchExpression matchExpression);

    void execute(AlertContext context);
    
}
