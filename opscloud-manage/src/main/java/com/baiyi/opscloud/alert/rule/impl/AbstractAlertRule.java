package com.baiyi.opscloud.alert.rule.impl;

import com.baiyi.opscloud.alert.rule.IRule;
import com.baiyi.opscloud.common.alert.AlertContext;
import com.baiyi.opscloud.common.alert.AlertRuleMatchExpression;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.domain.base.IInstanceType;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.facade.datasource.DsInstanceAssetFacade;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author 修远
 * @Date 2022/7/21 3:15 PM
 * @Since 1.0
 */

public abstract class AbstractAlertRule implements IRule, IInstanceType {

    @Value("${spring.profiles.active}")
    protected String env;

    @Resource
    protected DsConfigHelper dsConfigHelper;

    @Resource
    protected DsInstanceService dsInstanceService;

    @Resource
    protected DsInstanceAssetFacade dsInstanceAssetFacade;

    protected static Map<String, List<AlertRuleMatchExpression>> RULE_MAP = Maps.newHashMap();

    protected void refreshData() {
        RULE_MAP.clear();
    }

    public void preData() {
        refreshData();
        List<DatasourceInstance> datasourceInstances = dsInstanceService.listByInstanceType(getInstanceType());
        datasourceInstances.forEach(dsInstance -> {
            DatasourceConfig dsConfig = dsConfigHelper.getConfigByInstanceUuid(dsInstance.getUuid());
            preData(dsInstance, dsConfig);
        });
    }

    public void evaluate(DsAssetVO.Asset asset) {
        List<AlertRuleMatchExpression> alertRuleMatchExpressions = RULE_MAP.get(asset.getInstanceUuid());
        if (CollectionUtils.isEmpty(alertRuleMatchExpressions)) return;
        List<AlertRuleMatchExpression> matchExpressions = alertRuleMatchExpressions.stream()
                .sorted(Comparator.comparing(AlertRuleMatchExpression::getWeight).reversed())
                .collect(Collectors.toList());
        for (AlertRuleMatchExpression item : matchExpressions) {
            if (evaluate(asset, item)) {
                execute(converterContext(asset, item));
                break;
            }
        }
    }

    protected abstract AlertContext converterContext(DsAssetVO.Asset asset, AlertRuleMatchExpression matchExpression);

    protected abstract void preData(DatasourceInstance dsInstance, DatasourceConfig dsConfig);
}
