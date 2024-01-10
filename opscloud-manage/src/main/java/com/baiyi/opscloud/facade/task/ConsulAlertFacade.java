package com.baiyi.opscloud.facade.task;

import com.baiyi.opscloud.alert.rule.impl.AbstractAlertRule;
import com.baiyi.opscloud.alert.strategy.AlertStrategyFactory;
import com.baiyi.opscloud.alert.strategy.IAlertStrategy;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.ConsulConfig;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.datasource.consul.driver.ConsulServiceDriver;
import com.baiyi.opscloud.datasource.consul.entity.ConsulHealth;
import com.baiyi.opscloud.domain.alert.AlertContext;
import com.baiyi.opscloud.domain.alert.AlertNotifyMedia;
import com.baiyi.opscloud.domain.alert.AlertRuleMatchExpression;
import com.baiyi.opscloud.domain.alert.Metadata;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.user.UserPermissionService;
import com.baiyi.opscloud.service.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2023/2/24 15:17
 * @Version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class ConsulAlertFacade extends AbstractAlertRule {

    private final ApplicationService applicationService;

    private final UserPermissionService userPermissionService;

    private final UserService userService;

    private final ConsulServiceDriver consulServiceDriver;

    private static final String DATA_CENTER = "dc1";

    private static final String HEALTHY_STATUS = "passing";

    public void ruleEvaluate() {
        preData();
        List<DatasourceInstance> datasourceInstances = dsInstanceService.listByInstanceType(getInstanceType());
        datasourceInstances.forEach(dsInstance -> {
            DsAssetParam.AssetPageQuery pageQuery = DsAssetParam.AssetPageQuery.builder()
                    .assetType(DsAssetTypeConstants.CONSUL_SERVICE.name())
                    .extend(true)
                    .instanceId(dsInstance.getId())
                    .length(1000)
                    .page(1)
                    .queryName("")
                    .relation(false)
                    .build();
            List<DsAssetVO.Asset> assetList = dsInstanceAssetFacade.queryAssetPage(pageQuery).getData();
            if (CollectionUtils.isEmpty(assetList)) {
                return;
            }
            assetList.forEach(asset ->
                    evaluate(asset, getConfig(dsInstance.getUuid()).getStrategyMatchExpressions())
            );
        });
    }

    private ConsulConfig.Consul getConfig(String instanceUuid) {
        DatasourceConfig dsConfig = DS_CONFIG_MAP.get(instanceUuid);
        return dsConfigManager.build(dsConfig, ConsulConfig.class).getConsul();
    }

    @Override
    public Boolean evaluate(DsAssetVO.Asset asset, AlertRuleMatchExpression matchExpression) {
        if ("0".equals(asset.getProperties().get("checksCritical")))
            return false;
        List<ConsulHealth.Health> healthList = consulServiceDriver.listHealthService(getConfig(asset.getInstanceUuid()), asset.getName(), DATA_CENTER);
        List<String> warningNode = getWarningNode(healthList);
        if (CollectionUtils.isEmpty(warningNode))
            return false;
        double warningNum = warningNode.size();
        double totalNum = healthList.size();
        if (NumberUtils.isDigits(matchExpression.getValues()))
            return warningNum >= Integer.parseInt(matchExpression.getValues());
        try {
            double percent = NumberFormat.getPercentInstance().parse(matchExpression.getValues()).doubleValue();
            return warningNum / totalNum >= percent;
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    public List<String> getWarningNode(List<ConsulHealth.Health> healthList) {
        return healthList.stream()
                .filter(health -> health.getChecks().stream()
                        .anyMatch(check -> !HEALTHY_STATUS.equals(check.getStatus()))
                ).map(health -> health.getService().getAddress())
                .collect(Collectors.toList());
    }

    @Override
    protected AlertContext converterContext(DsAssetVO.Asset asset, AlertRuleMatchExpression matchExpression) {
        DatasourceInstance datasourceInstance = dsInstanceService.getByUuid(asset.getInstanceUuid());
        List<ConsulHealth.Health> healthList = consulServiceDriver.listHealthService(getConfig(asset.getInstanceUuid()), asset.getName(), DATA_CENTER);
        List<String> warningNode = getWarningNode(healthList);
        if (CollectionUtils.isEmpty(warningNode)) return null;
        List<Metadata> metadata = warningNode.stream()
                .map(node -> Metadata.builder()
                        .name(node)
                        .build()
                ).collect(Collectors.toList());
        return AlertContext.builder()
                .eventUuid(IdUtil.buildUUID())
                .alertName("Consul 节点异常告警")
                .severity(matchExpression.getSeverity())
                .message("Consul 不可用节点大于等于 " + matchExpression.getValues())
                .value(String.valueOf(warningNode.size()))
                .check(datasourceInstance.getInstanceName())
                .source(datasourceInstance.getUuid())
                .alertType(asset.getKind())
                .service(asset.getName())
                .alertTime(System.currentTimeMillis())
                .metadata(metadata)
                .build();
    }

    @Override
    public void execute(AlertContext context) {
        if (ObjectUtils.isEmpty(context)) {
            return;
        }
        IAlertStrategy alertStrategy = AlertStrategyFactory.getAlertActivity(context.getSeverity());
        Application application = applicationService.getByName(context.getService());
        if (ObjectUtils.isEmpty(application)) {
            return;
        }
        UserPermission userPermission = UserPermission.builder()
                .businessId(application.getId())
                .businessType(BusinessTypeEnum.APPLICATION.getType())
                .build();
        List<UserPermission> userPermissions = userPermissionService.queryByBusiness(userPermission).stream()
                .filter(x -> "admin".equals(x.getPermissionRole()))
                .toList();
        if (CollectionUtils.isEmpty(userPermissions)) {
            return;
        }
        List<User> users = userPermissions.stream()
                .map(permission -> userService.getById(permission.getUserId()))
                .filter(User::getIsActive)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(users)) {
            return;
        }
        ConsulConfig.Consul consul = getConfig(context.getSource());
        AlertNotifyMedia media = AlertNotifyMedia.builder()
                .users(users)
                .dingtalkToken(consul.getDingtalkToken())
                .ttsCode(consul.getTtsCode())
                .templateCode(consul.getTemplateCode())
                .build();
        alertStrategy.executeAlertStrategy(media, context);
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.CONSUL.name();
    }

}
