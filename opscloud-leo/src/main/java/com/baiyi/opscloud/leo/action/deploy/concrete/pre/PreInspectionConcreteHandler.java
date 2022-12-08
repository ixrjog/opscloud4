package com.baiyi.opscloud.leo.action.deploy.concrete.pre;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.leo.action.deploy.BaseDeployHandler;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.leo.LeoBuildImageService;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/12/6 09:48
 * @Version 1.0
 */
@Slf4j
@Component
public class PreInspectionConcreteHandler extends BaseDeployHandler {

    @Resource
    private DsInstanceAssetService assetService;

    @Resource
    private DsInstanceService instanceService;

    @Resource
    private LeoBuildImageService leoBuildImageService;

    @Resource
    private LeoBuildService leoBuildService;

    /**
     * 预检查
     *
     * @param leoDeploy
     * @param deployConfig
     */
    @Override
    protected void handle(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        LeoDeployModel.Deploy deploy = deployConfig.getDeploy();

        LeoBaseModel.Kubernetes kubernetes = Optional.ofNullable(deploy)
                .map(LeoDeployModel.Deploy::getKubernetes)
                .orElseThrow(() -> new LeoDeployException("Kubernetes配置不存在！"));

        Map<String, String> dict = Optional.of(deploy)
                .map(LeoDeployModel.Deploy::getDict)
                .orElseThrow(() -> new LeoDeployException("字典配置不存在！"));

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
        Deployment deployment = KubernetesDeploymentDriver.getDeployment(kubernetesConfig.getKubernetes(),
                kubernetesNamespace,
                deploymentName);

        final Integer replicas = deployment.getSpec().getReplicas();

        final String project = dict.containsKey(BuildDictConstants.PROJECT.getKey()) ?
                dict.get(BuildDictConstants.PROJECT.getKey()) : dict.get(BuildDictConstants.APPLICATION_NAME.getKey()).toLowerCase();

        Container container = deployment.getSpec().getTemplate().getSpec().getContainers()
                .stream()
                .filter(e -> e.getName().equals(project) || e.getName().equals(deploymentName))
                .findFirst()
                .orElseThrow(() -> new LeoDeployException("未找到容器: container={}", project));
        final String image = container.getImage();

        List<LeoBuildImage> leoBuildImages = leoBuildImageService.queryImageWithJobIdAndImage(leoDeploy.getJobId(), image);
        // 以前的版本
        LeoDeployModel.DeployVersion previousVersion;
        if (CollectionUtils.isEmpty(leoBuildImages)) {
            previousVersion = LeoDeployModel.DeployVersion.UNKNOWN;
            previousVersion.setImage(image);
        } else {
            LeoBuildImage leoBuildImage = leoBuildImages.get(0);
            previousVersion = LeoDeployModel.DeployVersion.builder()
                    .buildId(leoDeploy.getBuildId())
                    .versionName(leoBuildImage.getVersionName())
                    .versionDesc(leoBuildImage.getVersionDesc())
                    .image(leoBuildImage.getImage())
                    .build();
        }
        // 发布的版本
        LeoBuild leoBuild = leoBuildService.getById(leoDeploy.getBuildId());
        LeoDeployModel.DeployVersion releaseVersionVersion = LeoDeployModel.DeployVersion.builder()
                .buildId(leoDeploy.getBuildId())
                .versionName(leoBuild.getVersionName())
                .versionDesc(leoBuild.getVersionDesc())
                .image(dict.get(BuildDictConstants.IMAGE.getKey()))
                .build();

        if (releaseVersionVersion.getImage().equals(previousVersion.getImage())) {
            throw new LeoDeployException("预检查失败发布镜像未变更: image={}", releaseVersionVersion.getImage());
        }

        LeoBaseModel.Container containerModel = LeoBaseModel.Container.builder()
                .name(container.getName())
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
        deploy.setPreviousVersion(previousVersion);
        deploy.setReleaseVersion(releaseVersionVersion);

        LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                .id(leoDeploy.getId())
                .deployConfig(deployConfig.dump())
                .deployStatus("校验Kubernetes阶段: 成功")
                .build();
        save(saveLeoDeploy, "校验Kubernetes成功");
    }

}
