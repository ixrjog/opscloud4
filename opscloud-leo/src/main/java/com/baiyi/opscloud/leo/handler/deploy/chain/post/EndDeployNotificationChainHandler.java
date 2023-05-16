package com.baiyi.opscloud.leo.handler.deploy.chain.post;

import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.hook.leo.LeoHook;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.constants.DeployDictConstants;
import com.baiyi.opscloud.leo.constants.DeployStepConstants;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.handler.deploy.BaseDeployChainHandler;
import com.baiyi.opscloud.leo.handler.deploy.base.BaseDeployStrategy;
import com.baiyi.opscloud.leo.handler.deploy.base.DeployStrategyFactory;
import com.baiyi.opscloud.leo.handler.deploy.base.IDeployStep;
import com.baiyi.opscloud.leo.handler.deploy.chain.post.event.MeterSphereDeployEventPublisher;
import com.baiyi.opscloud.service.user.UserService;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/12/8 12:50
 * @Version 1.0
 */
@Slf4j
@Component
public class EndDeployNotificationChainHandler extends BaseDeployChainHandler implements IDeployStep {

    @Resource
    private MeterSphereDeployEventPublisher meterSphereDeployEventPublisher;

    @Resource
    private UserService userService;

    /**
     * 部署结束通知
     *
     * @param leoDeploy
     * @param deployConfig
     */
    @Override
    protected void handle(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        BaseDeployStrategy deployStrategy = DeployStrategyFactory.getStrategy(getStep(), deployConfig.getDeploy().getDeployType());
        // 基于策略模式实现
        deployStrategy.handleRequest(leoDeploy, deployConfig);
        // ms消息
        sendMeterSphereEvent(leoDeploy, deployConfig);
    }

    @Override
    public String getStep() {
        return DeployStepConstants.END_NOTIFICATION.name();
    }

    private void sendMeterSphereEvent(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        if (!leoDeploy.getIsActive()) {
            return;
        }
        try {
            Map<String, String> dict = Optional.ofNullable(deployConfig)
                    .map(LeoDeployModel.DeployConfig::getDeploy)
                    .map(LeoDeployModel.Deploy::getDict)
                    .orElse(Maps.newHashMap());

            User dbUser = userService.getByUsername(leoDeploy.getUsername());
            LeoHook.User user = LeoHook.User.builder()
                    .username(leoDeploy.getUsername())
                    .email(dbUser != null ? dbUser.getEmail() : "")
                    .displayName(dict.getOrDefault(DeployDictConstants.DISPLAY_NAME.getKey(), ""))
                    .build();

            String deployType = Optional.ofNullable(deployConfig)
                    .map(LeoDeployModel.DeployConfig::getDeploy)
                    .map(LeoDeployModel.Deploy::getDeployType)
                    .orElse("");

            LeoHook.DeployHook hook = LeoHook.DeployHook.builder()
                    .id(leoDeploy.getId())
                    .buildId(leoDeploy.getBuildId())
                    .name(leoDeploy.getJobName())
                    .deployType(deployType)
                    .appName(dict.getOrDefault(BuildDictConstants.APPLICATION_NAME.getKey(), ""))
                    .user(user)
                    .projectId(-1)
                    .pods(null)
                    .gmtModified(leoDeploy.getEndTime())
                    .build();

            meterSphereDeployEventPublisher.publish(hook);
        } catch (Exception ignored) {
        }
    }

}
