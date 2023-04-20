package com.baiyi.opscloud.leo.handler.deploy.strategy.notification.base;

import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.constants.DeployStepConstants;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/12/15 16:19
 * @Version 1.0
 */
public abstract class StartNotificationStrategy extends BaseNotificationStrategy {

    @Override
    public String getStep() {
        return DeployStepConstants.START_NOTIFICATION.name();
    }

    abstract protected String getMessageKey();

    /**
     * 部署开始通知
     *
     * @param leoDeploy
     * @param deployConfig
     */
    @Override
    protected void handle(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        try {
            sendMessage(leoDeploy, deployConfig);
            LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                    .id(leoDeploy.getId())
                    .deployStatus("启动部署通知阶段: 发送消息成功")
                    .build();
            save(saveLeoDeploy, "启动部署通知成功");
        } catch (LeoBuildException e) {
            LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                    .id(leoDeploy.getId())
                    .deployStatus("启动部署通知阶段: 发送消息失败")
                    .build();
            save(saveLeoDeploy, e.getMessage());
        }
    }

    private void sendMessage(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        Map<String, Object> contentMap = buildContentMap(leoDeploy, deployConfig);
        contentMap.put("deployPhase", "部署开始");
        sendMessage(leoDeploy, deployConfig, getMessageKey(), contentMap);
    }

}