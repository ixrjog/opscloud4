package com.baiyi.opscloud.leo.action.deploy.concrete.pre;

import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.action.deploy.BaseDeployHandler;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/12/8 11:03
 * @Version 1.0
 */
@Slf4j
@Component
public class StartDeployNotificationConcreteHandler extends BaseDeployHandler {

    private static final String LEO_DEPLOY_START = "LEO_DEPLOY_START";

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
            LeoDeploy saveLeoDeploy =  LeoDeploy.builder()
                    .id(leoDeploy.getId())
                    .deployStatus("启动部署通知阶段: 发送消息成功")
                    .build();
            save(saveLeoDeploy, "启动部署通知成功");
        } catch (LeoBuildException e) {
            LeoDeploy saveLeoDeploy =  LeoDeploy.builder()
                    .id(leoDeploy.getId())
                    .deployStatus("启动部署通知阶段: 发送消息失败")
                    .build();
            save(saveLeoDeploy, e.getMessage());
        }
    }

    private void sendMessage(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        Map<String, Object> contentMap = Maps.newHashMap();
        contentMap.put("deployPhase", "部署开始");
        sendMessage(leoDeploy, deployConfig, LEO_DEPLOY_START, contentMap);
    }

}
