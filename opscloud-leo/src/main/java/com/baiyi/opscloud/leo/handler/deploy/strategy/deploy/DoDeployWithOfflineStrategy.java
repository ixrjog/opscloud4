package com.baiyi.opscloud.leo.handler.deploy.strategy.deploy;

import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.constants.DeployTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.baiyi.opscloud.leo.handler.deploy.strategy.deploy.base.DoDeployStrategy;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/12/26 16:49
 * @Version 1.0
 */
@Slf4j
@Component
public class DoDeployWithOfflineStrategy extends DoDeployStrategy {

    /**
     * 执行部署
     *
     * @param leoDeploy
     * @param deployConfig
     * @param kubernetesConfig
     * @param deployment
     */
    @Override
    protected void doDeploy(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig, KubernetesConfig kubernetesConfig, Deployment deployment) {
        Optional.ofNullable(deployConfig)
                .map(LeoDeployModel.DeployConfig::getDeploy)
                .map(LeoDeployModel.Deploy::getKubernetes)
                .orElseThrow(() -> new LeoDeployException("Configuration does not exist: deploy->kubernetes"));

        Optional.of(deployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getReplicas)
                .orElseThrow(() -> new LeoDeployException("Read configuration error: deployment->spec->replicas"));
        // 设置副本数
        if (isEnvProd(deployConfig)) {
            throw new LeoDeployException("Prod environment is still offline, please contact the administrator for operation");
        }
        deployment.getSpec().setReplicas(0);
        try {
            KubernetesDeploymentDriver.update(kubernetesConfig.getKubernetes(), deployment);
            LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                    .id(leoDeploy.getId())
                    .startTime(new Date())
                    .deployConfig(deployConfig.dump())
                    .deployStatus("更新Deployment阶段: 成功")
                    .build();
            save(saveLeoDeploy, "更新Deployment成功");
        } catch (Exception e) {
            throw new LeoDeployException(e.getMessage());
        }
    }

    protected boolean isEnvProd(LeoDeployModel.DeployConfig deployConfig) {
        Map<String, String> dict = Optional.of(deployConfig)
                .map(LeoDeployModel.DeployConfig::getDeploy)
                .map(LeoDeployModel.Deploy::getDict)
                .orElse(Maps.newHashMap());
        boolean isProd = dict.containsKey(BuildDictConstants.ENV.getKey()) && Global.ENV_PROD.equalsIgnoreCase(dict.get(BuildDictConstants.ENV.getKey()));
        if (!isProd) {
            return false;
        } else {
            String deploymentName = Optional.of(deployConfig)
                    .map(LeoDeployModel.DeployConfig::getDeploy)
                    .map(LeoDeployModel.Deploy::getKubernetes)
                    .map(LeoBaseModel.Kubernetes::getDeployment)
                    .map(LeoBaseModel.Deployment::getName)
                    .orElse("");
            return !deploymentName.endsWith("-canary");
        }
    }

    @Override
    public String getDeployType() {
        return DeployTypeConstants.OFFLINE.name();
    }

}