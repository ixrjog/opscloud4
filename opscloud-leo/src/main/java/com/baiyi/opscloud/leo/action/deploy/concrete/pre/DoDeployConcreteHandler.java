package com.baiyi.opscloud.leo.action.deploy.concrete.pre;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.action.deploy.BaseDeployHandler;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/12/6 10:32
 * @Version 1.0
 */
@Slf4j
@Component
public class DoDeployConcreteHandler extends BaseDeployHandler {

    @Resource
    private DsInstanceAssetService assetService;

    @Resource
    private DsInstanceService instanceService;

    /**
     * 执行部署
     *
     * @param leoDeploy
     * @param deployConfig
     */
    @Override
    protected void handle(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {

        LeoBaseModel.Kubernetes kubernetes = Optional.ofNullable(deployConfig)
                .map(LeoDeployModel.DeployConfig::getDeploy)
                .map(LeoDeployModel.Deploy::getKubernetes)
                .orElseThrow(() -> new LeoDeployException("Kubernetes配置不存在！"));

        LeoDeployModel.DeployVersion releaseVersion = Optional.of(deployConfig)
                .map(LeoDeployModel.DeployConfig::getDeploy)
                .map(LeoDeployModel.Deploy::getReleaseVersion)
                .orElseThrow(() -> new LeoDeployException("发布版本配置不存在！"));


        final String instanceUuid = kubernetes.getInstance().getUuid();
        final String namespace = kubernetes.getDeployment().getNamespace();
        final String deploymentName = kubernetes.getDeployment().getName();
        final String containerName = kubernetes.getDeployment().getContainer().getName();
        KubernetesConfig kubernetesConfig = getKubernetesConfigWithUuid(instanceUuid);
        Deployment deployment = KubernetesDeploymentDriver.getDeployment(kubernetesConfig.getKubernetes(),
                namespace,
                deploymentName);
        if (deployment == null) {
            throw new LeoDeployException("Deployment不存在！");
        }

        Container container = deployment.getSpec().getTemplate().getSpec().getContainers()
                .stream()
                .filter(e -> e.getName().equals(containerName))
                .findFirst()
                .orElseThrow(() -> new LeoDeployException("未找到容器: container={}", containerName));
        container.setImage(releaseVersion.getImage());
        try {
            KubernetesDeploymentDriver.createOrReplaceDeployment(kubernetesConfig.getKubernetes(), namespace, deployment);
            LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                    .id(leoDeploy.getId())
                    .startTime(new Date())
                    .deployStatus("更新Deployment阶段: 成功")
                    .build();
            save(saveLeoDeploy, "更新Deployment成功");
        } catch (Exception e) {
            throw new LeoDeployException(e.getMessage());
        }
    }

}
