package com.baiyi.opscloud.facade.apollo.messenger;

import com.baiyi.opscloud.common.datasource.ApolloConfig;
import com.baiyi.opscloud.common.util.BeetlUtil;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.MessageTemplate;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;
import com.baiyi.opscloud.leo.exception.LeoInterceptorException;
import com.baiyi.opscloud.leo.handler.build.helper.ApplicationTagsHelper;
import com.baiyi.opscloud.leo.handler.build.helper.LeoRobotHelper;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.template.MessageTemplateService;
import com.baiyi.opscloud.service.user.UserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/11/13 09:37
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApolloReleaseMessenger {

    private final DsInstanceService dsInstanceService;

    private final MessageTemplateService msgTemplateService;

    private final UserService userService;

    private final ApplicationTagsHelper applicationTagsHelper;

    private final LeoRobotHelper leoRobotHelper;

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
            contentMap.put("ticketMsg", ticketId == 0 ? "变更窗口期内" : StringFormatter.format("No.{}", ticketId));
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

}