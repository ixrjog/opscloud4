package com.baiyi.opscloud.leo.handler.build;

import com.baiyi.opscloud.common.util.BeetlUtil;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.generator.opscloud.MessageTemplate;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.leo.handler.build.helper.LeoRobotHelper;
import com.baiyi.opscloud.service.template.MessageTemplateService;
import com.baiyi.opscloud.service.user.UserService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/7/13 20:48
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class BuildMessageCenter {

    private final LeoRobotHelper leoRobotHelper;

    private final MessageTemplateService msgTemplateService;

    private final UserService userService;

    public void sendMessage(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig, String messageKey, Map<String, Object> contentMap) {
        String dingtalkRobot = Optional.ofNullable(buildConfig)
                .map(LeoBuildModel.BuildConfig::getBuild)
                .map(LeoBuildModel.Build::getNotify)
                .map(LeoBaseModel.Notify::getName)
                .orElseThrow(() -> new LeoBuildException("Configuration does not exist: build->notify->name"));

        DatasourceInstance dsInstance = leoRobotHelper.getRobotInstance(dingtalkRobot);
        MessageTemplate messageTemplate = msgTemplateService.getByUniqueKey(messageKey, "DINGTALK_ROBOT", "markdown");
        if (messageTemplate == null) {
            throw new LeoBuildException("Sending message failed: message template does not exist!");
        }
        User user = userService.getByUsername(leoBuild.getUsername());

        contentMap.putAll(buildConfig.getBuild().getDict());
        contentMap.put("nowDate", NewTimeUtil.nowDate());
        contentMap.put("users", Lists.newArrayList(user));
        final String msg = renderTemplate(messageTemplate, contentMap);
        leoRobotHelper.send(dsInstance, msg);
    }

    protected String renderTemplate(MessageTemplate messageTemplate, Map<String, Object> contentMap) {
        try {
            return BeetlUtil.renderTemplate(messageTemplate.getMsgTemplate(), contentMap);
        } catch (IOException e) {
            throw new LeoBuildException("Error in rendering message template: {}", e.getMessage());
        }
    }

}