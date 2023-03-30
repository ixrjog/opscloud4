package com.baiyi.opscloud.leo.action.deploy.strategy.deploy;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.driver.NewKubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.constants.DeployTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.action.deploy.strategy.deploy.base.DoDeployStrategy;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
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

    @Override
    protected void doDeploy(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig, KubernetesConfig kubernetesConfig, Deployment deployment) {
        LeoBaseModel.Kubernetes kubernetes = Optional.ofNullable(deployConfig)
                .map(LeoDeployModel.DeployConfig::getDeploy)
                .map(LeoDeployModel.Deploy::getKubernetes)
                .orElseThrow(() -> new LeoDeployException("Kubernetes配置不存在！"));

        LeoDeployModel.DeployVersion releaseVersion = Optional.of(deployConfig)
                .map(LeoDeployModel.DeployConfig::getDeploy)
                .map(LeoDeployModel.Deploy::getDeployVersion2)
                .orElseThrow(() -> new LeoDeployException("发布版本配置不存在！"));

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
                .orElseThrow(() -> new LeoDeployException("读取副本数错误！"));
        if (replicas == 0) {
            /**
             * 为了安全考虑，副本数量+1，而不是设置为1
             */
            deployment.getSpec().setReplicas(replicas + 1);
        }
        try {
            NewKubernetesDeploymentDriver.update(kubernetesConfig.getKubernetes(), namespace, deployment);
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

}
