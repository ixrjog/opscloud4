package com.baiyi.opscloud.leo.action.deploy.strategy.notification.base;

import com.baiyi.opscloud.common.util.BeetlUtil;
import com.baiyi.opscloud.common.util.TimeUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.generator.opscloud.MessageTemplate;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.leo.action.build.helper.LeoRobotHelper;
import com.baiyi.opscloud.leo.action.deploy.base.BaseDeployStrategy;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.baiyi.opscloud.service.message.MessageTemplateService;
import com.baiyi.opscloud.service.user.UserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.annotation.Resource;
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
                .orElseThrow(() -> new LeoBuildException("发送消息失败: DingtalkRobot未配置！"));

        DatasourceInstance dsInstance = leoRobotHelper.getRobotInstance(dingtalkRobot);
        MessageTemplate messageTemplate = msgTemplateService.getByUniqueKey(messageKey, "DINGTALK_ROBOT", "markdown");
        if (messageTemplate == null)
            throw new LeoBuildException("发送消息失败: 消息模板未配置！");

        User user = userService.getByUsername(leoDeploy.getUsername());

        Map<String, Object> messageMap = Maps.newHashMap(deployConfig.getDeploy().getDict());
        messageMap.putAll(contentMap);
        messageMap.put("nowDate", TimeUtil.nowDate());
        messageMap.put("users", Lists.newArrayList(user));

        final String msg = renderTemplate(messageTemplate, messageMap);
        leoRobotHelper.send(dsInstance, msg);
    }

    private String renderTemplate(MessageTemplate messageTemplate, Map<String, Object> contentMap) {
        try {
            return BeetlUtil.renderTemplate(messageTemplate.getMsgTemplate(), contentMap);
        } catch (IOException e) {
            throw new LeoDeployException("渲染Dingtalk模板错误: err={}", e.getMessage());
        }
    }

}

