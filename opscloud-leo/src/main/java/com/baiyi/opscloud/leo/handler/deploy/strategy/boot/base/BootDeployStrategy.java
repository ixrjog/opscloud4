package com.baiyi.opscloud.leo.handler.deploy.strategy.boot.base;

import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.leo.handler.deploy.base.BaseDeployStrategy;
import com.baiyi.opscloud.leo.constants.DeployDictConstants;
import com.baiyi.opscloud.leo.constants.DeployStepConstants;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.service.user.UserService;

import jakarta.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/12/15 20:01
 * @Version 1.0
 */
public abstract class BootDeployStrategy extends BaseDeployStrategy {

    @Resource
    private UserService userService;

    abstract protected void preHandle(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig);

    /**
     * 执行部署
     *
     * @param leoDeploy
     * @param deployConfig
     */
    @Override
    protected void handle(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        preHandle(leoDeploy, deployConfig);
        Map<String, String> dict = deployConfig.getDeploy().getDict();
        final String username = leoDeploy.getUsername();
        User user = userService.getByUsername(username);
        final String displayName = Optional.ofNullable(user)
                .map(User::getDisplayName)
                .orElse(Optional.ofNullable(user)
                        .map(User::getName)
                        .orElse(username)
                );
        dict.put(DeployDictConstants.DISPLAY_NAME.getKey(), displayName);
        deployConfig.getDeploy().setDict(dict);
        LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                .id(leoDeploy.getId())
                .startTime(new Date())
                .deployConfig(deployConfig.dump())
                .deployStatus("BOOT阶段: 成功")
                .versionName(leoDeploy.getVersionName())
                .versionDesc(leoDeploy.getVersionDesc())
                .build();
        save(saveLeoDeploy, "BOOT成功");
    }

    @Override
    public String getStep() {
        return DeployStepConstants.BOOT.name();
    }

}