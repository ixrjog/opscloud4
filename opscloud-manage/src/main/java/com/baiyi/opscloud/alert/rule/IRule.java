package com.baiyi.opscloud.alert.rule;

import com.baiyi.opscloud.common.alert.AlertContext;
import com.baiyi.opscloud.common.alert.AlertRuleMatchExpression;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;

/**
 * @Author 修远
 * @Date 2022/7/21 2:38 PM
 * @Since 1.0
 */
public interface IRule {

    Boolean evaluate(DsAssetVO.Asset asset, AlertRuleMatchExpression matchExpression);

    void execute(AlertContext context);
}
