package com.baiyi.opscloud.leo.handler.deploy.chain.post;

import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.hook.leo.LeoHook;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.constants.DeployDictConstants;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.handler.deploy.BaseDeployChainHandler;
import com.baiyi.opscloud.leo.handler.deploy.chain.post.event.AliyunEventBridgeDeployEventPublisher;
import com.baiyi.opscloud.leo.handler.deploy.chain.post.event.MeterSphereDeployEventPublisher;
import com.baiyi.opscloud.leo.helper.LeoDeployPassCheck;
import com.baiyi.opscloud.service.user.UserService;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/5/16 11:34
 * @Version 1.0
 */
@Slf4j
@Component
public class DeployFinalProcessingChainHandler extends BaseDeployChainHandler {

    @Resource
    private MeterSphereDeployEventPublisher meterSphereDeployEventPublisher;

    @Resource
    private AliyunEventBridgeDeployEventPublisher aliyunEventBridgeDeployEventPublisher;

    @Resource
    private UserService userService;

    @Resource
    private LeoDeployPassCheck leoDeployPassCheck;

    /**
     * 部署结束通知
     *
     * @param leoDeploy
     * @param deployConfig
     */
    @Override
    protected void handle(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        // ms消息
        sendMeterSphereEvent(leoDeploy, deployConfig);
        // 颁发部署通行证
        if (leoDeploy.getIsActive()) {
            leoDeployPassCheck.issuePass(leoDeploy.getBuildId());
        }
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
                    .envName(dict.getOrDefault(BuildDictConstants.ENV.getKey(), ""))
                    .deployType(deployType)
                    .appName(dict.getOrDefault(BuildDictConstants.APPLICATION_NAME.getKey(), ""))
                    .appId(leoDeploy.getApplicationId())
                    .user(user)
                    .projectId(leoDeploy.getProjectId())
                    .pods(null)
                    .gmtModified(leoDeploy.getEndTime())
                    .imageTag(dict.getOrDefault(BuildDictConstants.IMAGE_TAG.getKey(), ""))
                    .build();

            meterSphereDeployEventPublisher.publish(hook);
            aliyunEventBridgeDeployEventPublisher.publish(hook);
        } catch (Exception ignored) {
        }
    }

}