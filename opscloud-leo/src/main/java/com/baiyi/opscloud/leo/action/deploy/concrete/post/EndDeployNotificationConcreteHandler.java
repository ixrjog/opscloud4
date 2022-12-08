package com.baiyi.opscloud.leo.action.deploy.concrete.post;

import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.action.build.helper.LeoRobotHelper;
import com.baiyi.opscloud.leo.action.deploy.BaseDeployHandler;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.service.message.MessageTemplateService;
import com.baiyi.opscloud.service.user.UserService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/12/8 12:50
 * @Version 1.0
 */
@Slf4j
@Component
public class EndDeployNotificationConcreteHandler extends BaseDeployHandler {

    @Resource
    private MessageTemplateService msgTemplateService;

    @Resource
    private LeoRobotHelper leoRobotHelper;

    @Resource
    private UserService userService;

    private static final String LEO_DEPLOY_END = "LEO_DEPLOY_END";

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
                    .deployStatus("结束部署通知阶段: 发送消息成功")
                    .build();
            save(saveLeoDeploy, "结束部署通知成功");
        } catch (LeoBuildException e) {
            LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                    .id(leoDeploy.getId())
                    .deployStatus("结束部署通知阶段: 发送消息失败")
                    .build();
            save(saveLeoDeploy, e.getMessage());
        }
    }

    private void sendMessage(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        Map<String, Object> contentMap = Maps.newHashMap();
        contentMap.put("deployPhase", "部署结束");
        contentMap.put("deployResult", leoDeploy.getDeployResult());
        sendMessage(leoDeploy, deployConfig, LEO_DEPLOY_END, contentMap);
    }

}
