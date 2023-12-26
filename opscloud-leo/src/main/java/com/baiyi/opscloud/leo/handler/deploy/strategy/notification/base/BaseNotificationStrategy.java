package com.baiyi.opscloud.leo.handler.deploy.strategy.notification.base;

import com.baiyi.opscloud.common.util.BeetlUtil;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.generator.opscloud.MessageTemplate;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.leo.handler.build.helper.LeoRobotHelper;
import com.baiyi.opscloud.leo.handler.deploy.base.BaseDeployStrategy;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.baiyi.opscloud.service.template.MessageTemplateService;
import com.baiyi.opscloud.service.user.UserService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import jakarta.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/12/16 14:12
 * @Version 1.0
 */
public abstract class BaseNotificationStrategy extends BaseDeployStrategy {

    @Resource
    private LeoRobotHelper leoRobotHelper;

    @Resource
    private MessageTemplateService msgTemplateService;

    @Resource
    private UserService userService;

    abstract protected String getMessageKey();

    abstract protected Map<String, Object> buildContentMap(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig);

    protected void sendMessage(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig, String messageKey, Map<String, Object> contentMap) {
        String dingtalkRobot = Optional.ofNullable(deployConfig)
                .map(LeoDeployModel.DeployConfig::getDeploy)
                .map(LeoDeployModel.Deploy::getNotify)
                .map(LeoBaseModel.Notify::getName)
                .orElseThrow(() -> new LeoBuildException("Configuration does not exist: deploy->notify->name"));

        DatasourceInstance dsInstance = leoRobotHelper.getRobotInstance(dingtalkRobot);
        MessageTemplate messageTemplate = msgTemplateService.getByUniqueKey(messageKey, "DINGTALK_ROBOT", "markdown");
        if (messageTemplate == null) {
            throw new LeoBuildException("Sending message failed: message template does not exist");
        }

        User user = userService.getByUsername(leoDeploy.getUsername());

        Map<String, Object> messageMap = Maps.newHashMap(deployConfig.getDeploy().getDict());
        messageMap.putAll(contentMap);
        messageMap.put("nowDate", NewTimeUtil.nowDate());
        messageMap.put("users", Lists.newArrayList(user));
        LeoBaseModel.Deployment deployment = deployConfig.getDeploy().getKubernetes().getDeployment();
        messageMap.put("envGroup", Joiner.on(":").join(deployment.getNamespace(), deployment.getName()));

        final String msg = renderTemplate(messageTemplate, messageMap);
        leoRobotHelper.send(dsInstance, msg);
    }

    private String renderTemplate(MessageTemplate messageTemplate, Map<String, Object> contentMap) {
        try {
            return BeetlUtil.renderTemplate(messageTemplate.getMsgTemplate(), contentMap);
        } catch (IOException e) {
            throw new LeoDeployException("渲染Dingtalk模板错误: {}", e.getMessage());
        }
    }

}