package com.baiyi.opscloud.leo.handler.deploy.strategy.inspection.base;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.handler.deploy.base.BaseDeployStrategy;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.constants.DeployStepConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.leo.LeoBuildImageService;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.apps.Deployment;

import jakarta.annotation.Resource;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/12/12 20:37
 * @Version 1.0
 */
public abstract class BasePreInspectionStrategy extends BaseDeployStrategy {

    @Resource
    private DsInstanceAssetService assetService;

    @Resource
    private DsInstanceService instanceService;

    @Resource
    protected LeoBuildImageService leoBuildImageService;

    @Resource
    protected LeoBuildService leoBuildService;

    @Override
    public String getStep() {
        return DeployStepConstants.PRE_INSPECTION.name();
    }

    /**
     * 预检查
     *
     * @param leoDeploy
     * @param deployConfig
     */
    protected void preHandle(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        LeoDeployModel.Deploy deploy = deployConfig.getDeploy();

        LeoBaseModel.Kubernetes kubernetes = Optional.ofNullable(deploy)
                .map(LeoDeployModel.Deploy::getKubernetes)
                .orElseThrow(() -> new LeoDeployException("Configuration does not exist: deploy->kubernetes"));

        Map<String, String> dict = Optional.of(deploy)
                .map(LeoDeployModel.Deploy::getDict)
                .orElseThrow(() -> new LeoDeployException("Configuration does not exist: deploy->dict"));

        DatasourceInstanceAsset asset = assetService.getById(kubernetes.getAssetId());
        if (asset == null) {
            throw new LeoDeployException("数据源资产不存在: assetId={}", kubernetes.getAssetId());
        }
        final String instanceUuid = asset.getInstanceUuid();
        final String kubernetesNamespace = asset.getAssetKey2();
        final String deploymentName = asset.getAssetKey();
        DatasourceInstance dsInstance = instanceService.getByUuid(instanceUuid);
        LeoBaseModel.DsInstance kubernetesInstance = LeoBaseModel.DsInstance.builder()
                .uuid(instanceUuid)
                .name(dsInstance.getInstanceName())
                .build();

        KubernetesConfig kubernetesConfig = getKubernetesConfigWithUuid(dsInstance.getUuid());
        Deployment deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(),
                kubernetesNamespace,
                deploymentName);

        int replicas = deployment.getSpec().getReplicas();
        if (replicas == 0) {
            replicas = 1;
        }
        
        final String project = dict.containsKey(BuildDictConstants.PROJECT.getKey()) ?
                dict.get(BuildDictConstants.PROJECT.getKey()) : dict.get(BuildDictConstants.APPLICATION_NAME.getKey()).toLowerCase();

        Container container = deployment.getSpec().getTemplate().getSpec().getContainers()
                .stream()
                .filter(e -> e.getName().equals(project) || e.getName().equals(deploymentName))
                .findFirst()
                .orElseThrow(() -> new LeoDeployException("未找到容器: container={}", project));
        final String image = container.getImage();
        LeoBaseModel.Container containerModel = LeoBaseModel.Container.builder()
                .name(container.getName())
                .image(image)
                .build();

        LeoBaseModel.Deployment deploymentModel = LeoBaseModel.Deployment.builder()
                .name(deploymentName)
                .replicas(replicas)
                .namespace(kubernetesNamespace)
                .container(containerModel)
                .build();

        LeoBaseModel.Kubernetes kubernetesModel = LeoBaseModel.Kubernetes.builder()
                .instance(kubernetesInstance)
                .deployment(deploymentModel)
                .assetId(asset.getId())
                .build();

        deploy.setKubernetes(kubernetesModel);

        LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                .id(leoDeploy.getId())
                .deployConfig(deployConfig.dump())
                .build();
        save(saveLeoDeploy);
    }

}