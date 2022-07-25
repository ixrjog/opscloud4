package com.baiyi.opscloud.alert.rule.impl;

import com.baiyi.opscloud.alert.strategy.AlertStrategyFactory;
import com.baiyi.opscloud.alert.strategy.IAlertStrategy;
import com.baiyi.opscloud.common.alert.AlertContext;
import com.baiyi.opscloud.common.alert.AlertNotifyMedia;
import com.baiyi.opscloud.common.alert.AlertRuleMatchExpression;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.ConsulConfig;
import com.baiyi.opscloud.domain.annotation.InstanceHealth;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.user.UserPermissionService;
import com.baiyi.opscloud.service.user.UserService;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.common.base.Global.ENV_PROD;

/**
 * @Author 修远
 * @Date 2022/7/21 5:14 PM
 * @Since 1.0
 */

@Slf4j
@Component
public class ConsulAlertRule extends AbstractAlertRule {

    @Resource
    private ApplicationService applicationService;

    @Resource
    private UserPermissionService userPermissionService;

    @Resource
    private UserService userService;

    /**
     * Pair<dingTalkToken, ttsCode>
     */
    private static Map<String, Pair<String, String>> DINGTALK_TOKEN_MAP = Maps.newHashMap();

    @InstanceHealth // 实例健康检查，高优先级
    @Scheduled(cron = "10 */1 * * * ?")
    @SchedulerLock(name = "consul_alert_rule_evaluate_task", lockAtMostFor = "1m", lockAtLeastFor = "1m")
    public void ruleEvaluate() {
        // 非生产环境不执行任务
        if (ENV_PROD.equals(env)) {
            log.info("consul 告警规则评估");
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
                assetList.forEach(this::evaluate);
            });
            log.info("consul 告警规则结束");
        }
    }

    @Override
    protected void refreshData() {
        super.refreshData();
        DINGTALK_TOKEN_MAP.clear();
    }

    @Override
    protected void preData(DatasourceInstance dsInstance, DatasourceConfig dsConfig) {
        ConsulConfig.Consul config = dsConfigHelper.build(dsConfig, ConsulConfig.class).getConsul();
        RULE_MAP.put(dsInstance.getUuid(), config.getStrategyMatchExpressions());
        Pair<String, String> pair = new ImmutablePair<>(config.getDingtalkToken(), config.getTtsCode());
        DINGTALK_TOKEN_MAP.put(dsInstance.getUuid(), pair);
    }

    @Override
    public Boolean evaluate(DsAssetVO.Asset asset, AlertRuleMatchExpression matchExpression) {
        double warningNum = Double.parseDouble(asset.getProperties().get("checksCritical"));
        if (NumberUtils.isDigits(matchExpression.getValues()))
            return warningNum >= Integer.parseInt(matchExpression.getValues());
        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<List<String>>() {}.getType();
        List<String> strings = gson.fromJson(asset.getProperties().get("nodes"), type);
        int totalNum = strings.size();
        try {
            double percent = NumberFormat.getPercentInstance().parse(matchExpression.getValues()).doubleValue();
            return warningNum / totalNum >= percent;
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    protected AlertContext converterContext(DsAssetVO.Asset asset, AlertRuleMatchExpression matchExpression) {
        DatasourceInstance datasourceInstance = dsInstanceService.getByUuid(asset.getInstanceUuid());
        return AlertContext.builder()
                .alertName("Consul 节点异常告警")
                .severity(matchExpression.getSeverity())
                .message("Consul 不可用节点大于 " + matchExpression.getValues())
                .value(asset.getProperties().get("checksCritical"))
                .check(datasourceInstance.getInstanceName())
                .source(datasourceInstance.getUuid())
                .alertType(asset.getKind())
                .service(asset.getName())
                .alertTime(System.currentTimeMillis())
                .build();
    }

    @Override
    public void execute(AlertContext context) {
        IAlertStrategy alertStrategy = AlertStrategyFactory.getAlertActivity(context.getSeverity());
        Application application = applicationService.getByName(context.getService());
        UserPermission userPermission = UserPermission.builder()
                .businessId(application.getId())
                .businessType(BusinessTypeEnum.APPLICATION.getType())
                .build();
        List<UserPermission> userPermissions = userPermissionService.queryByBusiness(userPermission).stream()
                .filter(x -> "admin".equals(x.getPermissionRole()))
                .collect(Collectors.toList());
        List<User> users = userPermissions.stream()
                .map(permission -> userService.getById(permission.getUserId()))
                .filter(User::getIsActive)
                .collect(Collectors.toList());
        AlertNotifyMedia media = AlertNotifyMedia.builder()
                .users(users)
                .dingtalkToken(DINGTALK_TOKEN_MAP.get(context.getSource()).getLeft())
                .ttsCode(DINGTALK_TOKEN_MAP.get(context.getSource()).getRight())
                .build();
        alertStrategy.executeAlertStrategy(media, context);
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.CONSUL.name();
    }
}
