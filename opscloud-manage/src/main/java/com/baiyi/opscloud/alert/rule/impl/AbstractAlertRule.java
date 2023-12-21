package com.baiyi.opscloud.alert.rule.impl;

import com.baiyi.opscloud.alert.rule.IRule;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.domain.alert.AlertContext;
import com.baiyi.opscloud.domain.alert.AlertRuleMatchExpression;
import com.baiyi.opscloud.domain.base.IInstanceType;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.facade.datasource.DsInstanceAssetFacade;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @Author 修远
 * @Date 2022/7/21 3:15 PM
 * @Since 1.0
 */

@Slf4j
public abstract class AbstractAlertRule implements IRule, IInstanceType {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    protected DsConfigManager dsConfigManager;

    @Resource
    protected DsInstanceService dsInstanceService;

    @Resource
    protected DsInstanceAssetFacade dsInstanceAssetFacade;

    private static final String PREFIX = "alert_rule";

    protected static Map<String, DatasourceConfig> DS_CONFIG_MAP = Maps.newHashMap();

    public void preData() {
        DS_CONFIG_MAP.clear();
        List<DatasourceInstance> datasourceInstances = dsInstanceService.listByInstanceType(getInstanceType());
        datasourceInstances.forEach(dsInstance -> {
            DatasourceConfig dsConfig = dsConfigManager.getConfigByInstanceUuid(dsInstance.getUuid());
            DS_CONFIG_MAP.put(dsInstance.getUuid(), dsConfig);
        });
    }

    public void evaluate(DsAssetVO.Asset asset, List<AlertRuleMatchExpression> alertRuleMatchExpressions) {
        if (CollectionUtils.isEmpty(alertRuleMatchExpressions)) {
            return;
        }
        List<AlertRuleMatchExpression> matchExpressions = alertRuleMatchExpressions.stream()
                .sorted(Comparator.comparing(AlertRuleMatchExpression::getWeight).reversed())
                .toList();
        for (AlertRuleMatchExpression item : matchExpressions) {
            if (evaluate(asset, item)) {
                if (!failureDeadline(asset, item)) {
                    break;
                }
                if (silence(asset, item)) {
                    break;
                }
                execute(converterContext(asset, item));
                break;
            }
        }
    }

    protected abstract AlertContext converterContext(DsAssetVO.Asset asset, AlertRuleMatchExpression matchExpression);

    protected String getCacheKeyPrefix(DsAssetVO.Asset asset) {
        return Joiner.on("#").join(PREFIX, getInstanceType().toLowerCase(), asset.getInstanceUuid(), asset.getAssetKey());
    }

    @Override
    public Boolean failureDeadline(DsAssetVO.Asset asset, AlertRuleMatchExpression matchExpression) {
        // 无容忍，直接告警
        if (matchExpression.getFailureThreshold() == 0 || matchExpression.getFailureThreshold() == 1) {
            return true;
        }
        String cacheKey = Joiner.on("#").join(getCacheKeyPrefix(asset), matchExpression.getWeight(), "failureThreshold");
        if (redisUtil.hasKey(cacheKey)) {
            Integer count = (Integer) redisUtil.get(cacheKey);
            if (count >= matchExpression.getFailureThreshold() - 1) {
                log.info("达到错误阈值: key={}", cacheKey);
                return true;
            }
            redisUtil.incr(cacheKey, 1);
            log.info("触发告警: key={}, cnt={}", cacheKey, count + 1);
            return false;
        }
        redisUtil.set(cacheKey, 1, 60L * matchExpression.getFailureThreshold() + 30L);
        log.info("触发告警: key={}, cnt={}", cacheKey, 1);
        return false;
    }

    @Override
    public Boolean silence(DsAssetVO.Asset asset, AlertRuleMatchExpression matchExpression) {
        String cacheKey = Joiner.on("#").join(getCacheKeyPrefix(asset), matchExpression.getWeight());
        if (redisUtil.hasKey(cacheKey)) {
            log.debug("告警规则静默中: key={}", cacheKey);
            return true;
        }
        redisUtil.set(cacheKey, true, matchExpression.getSilenceSeconds());
        return false;
    }

}