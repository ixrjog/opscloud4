package com.baiyi.opscloud.leo.handler.deploy.strategy.deploy;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.constants.DeployTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.baiyi.opscloud.leo.handler.deploy.strategy.deploy.base.DoDeployStrategy;
import com.google.common.collect.Lists;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/12/14 10:47
 * @Version 1.0
 */
@Slf4j
@Component
public class DoDeployWithRollingStrategy extends DoDeployStrategy {

    @Override
    public String getDeployType() {
        return DeployTypeConstants.ROLLING.name();
    }

    private static final String OC_BUILD_ID = "OC_BUILD_ID";

    @Override
    protected void doDeploy(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig, KubernetesConfig kubernetesConfig, Deployment deployment) {
        LeoBaseModel.Kubernetes kubernetes = Optional.ofNullable(deployConfig)
                .map(LeoDeployModel.DeployConfig::getDeploy)
                .map(LeoDeployModel.Deploy::getKubernetes)
                .orElseThrow(() -> new LeoDeployException("Configuration does not exist: deploy->kubernetes"));

        LeoDeployModel.DeployVersion releaseVersion = Optional.of(deployConfig)
                .map(LeoDeployModel.DeployConfig::getDeploy)
                .map(LeoDeployModel.Deploy::getDeployVersion2)
                .orElseThrow(() -> new LeoDeployException("Configuration does not exist: deploy->deployVersion2"));

        final String containerName = kubernetes.getDeployment().getContainer().getName();
        final String namespace = kubernetes.getDeployment().getNamespace();

        Container container = deployment.getSpec().getTemplate().getSpec().getContainers()
                .stream()
                .filter(e -> e.getName().equals(containerName))
                .findFirst()
                .orElseThrow(() -> new LeoDeployException("未找到容器: container={}", containerName));
        container.setImage(releaseVersion.getImage());

        final int replicas = Optional.of(deployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getReplicas)
                .orElseThrow(() -> new LeoDeployException("Read configuration error: deployment->spec->replicas"));
        if (replicas == 0) {
            deployment.getSpec().setReplicas(calcReplicas(deployConfig));
        }
        // Env写入buildId
        writeEnv(container, leoDeploy.getBuildId());
        try {
            KubernetesDeploymentDriver.update(kubernetesConfig.getKubernetes(), namespace, deployment);
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

    private void writeEnv(Container container, int buildId) {
        EnvVar buildIdEnvVar = new EnvVar(OC_BUILD_ID, String.valueOf(buildId), null);
        List<EnvVar> newEnvVars = Lists.newArrayList(buildIdEnvVar);
        container.getEnv().stream().filter(envVar -> !envVar.getName().equals(OC_BUILD_ID)).forEach(newEnvVars::add);
        container.getEnv().clear();
        container.setEnv(newEnvVars);
    }

}