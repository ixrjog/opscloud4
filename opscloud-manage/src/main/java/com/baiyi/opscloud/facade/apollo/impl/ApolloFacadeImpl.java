package com.baiyi.opscloud.facade.apollo.impl;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.ApolloConfig;
import com.baiyi.opscloud.common.holder.WorkOrderApolloReleaseHolder;
import com.baiyi.opscloud.common.util.BeetlUtil;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.datasource.apollo.entity.InterceptRelease;
import com.baiyi.opscloud.datasource.apollo.provider.ApolloInterceptReleaseProvider;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.model.WorkOrderToken;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;
import com.baiyi.opscloud.facade.apollo.ApolloFacade;
import com.baiyi.opscloud.facade.apollo.ApolloRuleValidator;
import com.baiyi.opscloud.leo.domain.model.LeoRuleModel;
import com.baiyi.opscloud.leo.exception.LeoInterceptorException;
import com.baiyi.opscloud.leo.handler.build.helper.ApplicationTagsHelper;
import com.baiyi.opscloud.leo.handler.build.helper.LeoRobotHelper;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.leo.LeoRuleService;
import com.baiyi.opscloud.service.template.MessageTemplateService;
import com.baiyi.opscloud.service.user.UserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;

/**
 * @Author baiyi
 * @Date 2023/5/30 14:10
 * @Version 1.0
 */
@SuppressWarnings("rawtypes")
@Slf4j
@AllArgsConstructor
@Component
public class ApolloFacadeImpl implements ApolloFacade {

    private final DsConfigService dsConfigService;

    private final DsConfigManager dsConfigManager;

    private final ApolloRuleValidator apolloRuleValidator;

    private final LeoRuleService leoRuleService;

    private final ApolloInterceptReleaseProvider apolloInterceptReleaseProvider;

    private final DsInstanceService dsInstanceService;

    private final WorkOrderApolloReleaseHolder workOrderApolloReleaseHolder;

    private final ApplicationService applicationService;

    private final MessageTemplateService msgTemplateService;

    private final UserService userService;

    private final ApplicationTagsHelper applicationTagsHelper;

    private final LeoRobotHelper leoRobotHelper;


    private static final int NO_WORK_ORDER_ID = 0;

    @Override
    public HttpResult interceptRelease(ApolloParam.ReleaseEvent releaseEvent) {
        List<DatasourceConfig> dsConfigs = dsConfigService.queryByDsType(DsTypeEnum.APOLLO.getType());
        // 通过Token过滤实例
        Optional<ApolloConfig> optionalConfig = dsConfigs.stream()
                .map(config -> dsConfigManager.build(config, ApolloConfig.class))
                .toList()
                .stream()
                .filter(e -> e.getApollo().filter(releaseEvent.getToken()))
                .findAny();

        // 检查Token
        if (optionalConfig.isEmpty()) {
            log.debug("Invalid Apollo interceptor token");
            return HttpResult.SUCCESS;
        }

        ApolloConfig apolloConfig = optionalConfig.get();

        // 需要拦截的环境
        List<String> envs = Optional.of(apolloConfig.getApollo())
                .map(ApolloConfig.Apollo::getPortal)
                .map(ApolloConfig.Portal::getRelease)
                .map(ApolloConfig.Release::getInterceptor)
                .map(ApolloConfig.Interceptor::getEnvs)
                .orElse(Collections.emptyList());
        // 环境配置不存在
        if (CollectionUtils.isEmpty(envs)) {
            log.debug("Interceptor environment configuration is empty");
            return HttpResult.SUCCESS;
        }

        Application application = applicationService.getByName(releaseEvent.getAppId());
        // 应用不存在
        if (application == null) {
            return HttpResult.SUCCESS;
        }

        // 白名单规则校验
        if (workOrderApolloReleaseHolder.hasKey(application.getId())) {
            WorkOrderToken.ApolloReleaseToken token = workOrderApolloReleaseHolder.getToken(application.getId());
            consumeAsset(apolloConfig, releaseEvent, token.getTicketId(), application);
            return HttpResult.SUCCESS;
        }

        // 规则表达式校验
        HttpResult httpResult;
        try {
            verifyRule(releaseEvent);
            consumeAsset(apolloConfig, releaseEvent, NO_WORK_ORDER_ID, application);
            httpResult = HttpResult.SUCCESS;
        } catch (LeoInterceptorException e) {
            httpResult = HttpResult.builder()
                    .success(false)
                    .code(SC_BAD_REQUEST)
                    .msg(e.getMessage())
                    .build();
            consumeAsset(apolloConfig, releaseEvent, httpResult, NO_WORK_ORDER_ID);
            return httpResult;
        }
        return httpResult;
    }

    /**
     * 记录
     *
     * @param apolloConfig
     * @param releaseEvent
     * @param ticketId
     */
    private void consumeAsset(ApolloConfig apolloConfig, ApolloParam.ReleaseEvent releaseEvent, Integer ticketId, Application application) {
        this.consumeAsset(apolloConfig, releaseEvent, HttpResult.SUCCESS, ticketId);
        this.notify(apolloConfig, releaseEvent, ticketId, application);
    }

    /**
     * 记录
     *
     * @param apolloConfig
     * @param releaseEvent
     * @param httpResult
     * @param ticketId
     */
    private void consumeAsset(ApolloConfig apolloConfig, ApolloParam.ReleaseEvent releaseEvent, HttpResult httpResult, Integer ticketId) {
        DatasourceInstance datasourceInstance = dsInstanceService.getByConfigId(apolloConfig.getConfigId());
        if (datasourceInstance == null) {
            return;
        }
        InterceptRelease.Event event = InterceptRelease.Event.builder()
                .appId(releaseEvent.getAppId())
                .env(releaseEvent.getEnv())
                .clusterName(releaseEvent.getClusterName())
                .namespaceName(releaseEvent.getNamespaceName())
                .isGray(releaseEvent.getIsGray())
                .username(releaseEvent.getUsername())
                .branchName(releaseEvent.getBranchName())
                .releaseTitle(releaseEvent.getReleaseTitle())
                .success(httpResult.isSuccess())
                .code(httpResult.getCode())
                .msg(httpResult.getMsg())
                .ticketId(ticketId)
                .build();
        // 写入资产
        apolloInterceptReleaseProvider.pullAsset(datasourceInstance.getId(), event);
    }

    @Override
    public void notify(ApolloConfig apolloConfig, ApolloParam.ReleaseEvent releaseEvent, Integer ticketId, Application application) {
        if (!releaseEvent.getEnv().equalsIgnoreCase("PROD")) {
            return;
        }
        DatasourceInstance datasourceInstance = dsInstanceService.getByConfigId(apolloConfig.getConfigId());
        // 发送通知
        try {
            MessageTemplate messageTemplate = msgTemplateService.getByUniqueKey("APOLLO_RELEASE", "DINGTALK_ROBOT", "markdown");
            Map<String, Object> contentMap = Maps.newHashMap();
            User user = userService.getByUsername(releaseEvent.getUsername());
            applicationTagsHelper.getTagsStr(application.getId());
            contentMap.put("applicationName", application.getName());
            contentMap.put("nowDate", NewTimeUtil.nowDate());
            contentMap.put("applicationTags", applicationTagsHelper.getTagsStr(application.getId()));
            contentMap.put("instanceName", datasourceInstance.getInstanceName());
            contentMap.put("env", releaseEvent.getEnv().toLowerCase());
            contentMap.put("isGray", releaseEvent.getIsGray() ? "是" : "否");
            contentMap.put("ticketMsg", ticketId == 0 ? "未关联配置发布工单" : StringFormatter.format("配置发布工单ID={}", ticketId));
            contentMap.put("displayName", user.getDisplayName());
            contentMap.put("users", Lists.newArrayList(user));
            final String msg = BeetlUtil.renderTemplate(messageTemplate.getMsgTemplate(), contentMap);

            String dingtalkRobot = Optional.of(apolloConfig)
                    .map(ApolloConfig::getApollo)
                    .map(ApolloConfig.Apollo::getPortal)
                    .map(ApolloConfig.Portal::getRelease)
                    .map(ApolloConfig.Release::getNotify)
                    .orElseThrow(() -> new LeoInterceptorException("Configuration does not exist: apollo->portal->release->notify"));

            DatasourceInstance dsInstance = leoRobotHelper.getRobotInstance(dingtalkRobot);
            leoRobotHelper.send(dsInstance, msg);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    /**
     * 校验
     */
    public void verifyRule(ApolloParam.ReleaseEvent releaseEvent) {
        final String env = releaseEvent.getEnv().toLowerCase();
        List<LeoRule> rules = leoRuleService.queryAll();
        for (LeoRule rule : rules) {
            LeoRuleModel.RuleConfig ruleConfig = LeoRuleModel.load(rule);
            List<String> envs = Optional.ofNullable(ruleConfig)
                    .map(LeoRuleModel.RuleConfig::getRule)
                    .map(LeoRuleModel.Rule::getEnvs)
                    .orElse(Collections.emptyList());
            if (!CollectionUtils.isEmpty(envs)) {
                if (envs.stream().anyMatch(e -> e.equalsIgnoreCase(env))) {
                    try {
                        apolloRuleValidator.verify(releaseEvent, rule);
                    } catch (LeoInterceptorException e) {
                        throw new LeoInterceptorException("当前规则禁止执行: {}！", rule.getName());
                    }
                }
            }
        }
    }

}
