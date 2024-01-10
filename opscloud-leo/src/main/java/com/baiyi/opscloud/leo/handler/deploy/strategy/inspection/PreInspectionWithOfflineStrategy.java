package com.baiyi.opscloud.leo.handler.deploy.strategy.inspection;

import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.domain.constants.DeployTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.handler.deploy.strategy.inspection.base.BasePreInspectionStrategy;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * 下线预检查策略
 *
 * @Author baiyi
 * @Date 2022/12/13 15:16
 * @Version 1.0
 */
@Slf4j
@Component
public class PreInspectionWithOfflineStrategy extends BasePreInspectionStrategy {

    /**
     * 预检查
     *
     * @param leoDeploy
     * @param deployConfig
     */
    @Override
    protected void handle(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        preHandle(leoDeploy, deployConfig);
        LeoDeployModel.Deploy deploy = deployConfig.getDeploy();

        // image
        Optional.of(deploy)
                .map(LeoDeployModel.Deploy::getKubernetes)
                .map(LeoBaseModel.Kubernetes::getDeployment)
                .map(LeoBaseModel.Deployment::getContainer)
                .map(LeoBaseModel.Container::getImage)
                .orElseThrow(() -> new LeoDeployException("Configuration does not exist: deploy->kubernetes->deployment->container->image"));

        Map<String, String> dict = Optional.of(deploy)
                .map(LeoDeployModel.Deploy::getDict)
                .orElseThrow(() -> new LeoDeployException("Configuration does not exist: deploy->dict"));

        LeoBaseModel.Kubernetes kubernetes = Optional.of(deploy)
                .map(LeoDeployModel.Deploy::getKubernetes)
                .orElseThrow(() -> new LeoDeployException("Configuration does not exist: deploy->kubernetes"));

        final String deploymentName = kubernetes.getDeployment().getName();
        // 校验
        if (dict.containsKey(BuildDictConstants.ENV.getKey())) {
            // env = prod
            if (dict.get(BuildDictConstants.ENV.getKey()).equals(Global.ENV_PROD)) {
                if (!deploymentName.endsWith("-canary")) {
                    throw new LeoDeployException("禁止生产环境下线！");
                }
            }
        } else {
            throw new LeoDeployException("Configuration does not exist: deploy->dict->env");
        }

        LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                .id(leoDeploy.getId())
                .deployConfig(deployConfig.dump())
                .deployStatus("校验Kubernetes阶段: 成功")
                .build();
        save(saveLeoDeploy, "校验Kubernetes成功");
    }

    @Override
    public String getDeployType() {
        return DeployTypeConstants.OFFLINE.name();
    }

}