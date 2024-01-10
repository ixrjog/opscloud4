package com.baiyi.opscloud.leo.handler.deploy.strategy.inspection;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesPodDriver;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuildImage;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.vo.leo.LeoDeployingVO;
import com.baiyi.opscloud.leo.handler.deploy.strategy.inspection.base.BasePreInspectionStrategy;
import com.baiyi.opscloud.domain.constants.DeployTypeConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.baiyi.opscloud.leo.helper.PodDetailsHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 重启预检查策略
 * @Author baiyi
 * @Date 2022/12/12 20:33
 * @Version 1.0
 */
@Slf4j
@Component
public class PreInspectionWithRedeployStrategy extends BasePreInspectionStrategy {

    @Resource
    private PodDetailsHelper podDetailsHelper;

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

        final String image = Optional.of(deploy)
                .map(LeoDeployModel.Deploy::getKubernetes)
                .map(LeoBaseModel.Kubernetes::getDeployment)
                .map(LeoBaseModel.Deployment::getContainer)
                .map(LeoBaseModel.Container::getImage)
                .orElseThrow(() -> new LeoDeployException("Configuration does not exist: deploy->kubernetes->deployment->container->image"));

        Map<String, String> dict = Optional.of(deploy)
                .map(LeoDeployModel.Deploy::getDict)
                .orElseThrow(() -> new LeoDeployException("Configuration does not exist: deploy->dict"));

        List<LeoBuildImage> leoBuildImages = leoBuildImageService.queryImageWithJobIdAndImage(leoDeploy.getJobId(), image);
        // 滚动前
        LeoDeployModel.DeployVersion oldVersion;
        if (CollectionUtils.isEmpty(leoBuildImages)) {
            oldVersion = LeoDeployModel.DeployVersion.UNKNOWN;
            oldVersion.setImage(image);
        } else {
            LeoBuildImage leoBuildImage = leoBuildImages.getFirst();
            oldVersion = LeoDeployModel.DeployVersion.builder()
                    .buildId(leoDeploy.getBuildId())
                    .image(leoBuildImage.getImage())
                    .build();
        }

        LeoBaseModel.Kubernetes kubernetes = Optional.of(deploy)
                .map(LeoDeployModel.Deploy::getKubernetes)
                .orElseThrow(() -> new LeoDeployException("Configuration does not exist: deploy->kubernetes"));

        final String instanceUuid = kubernetes.getInstance().getUuid();
        final String namespace = kubernetes.getDeployment().getNamespace();
        final String deploymentName = kubernetes.getDeployment().getName();
        final String containerName = kubernetes.getDeployment().getContainer().getName();
        KubernetesConfig kubernetesConfig = getKubernetesConfigWithUuid(instanceUuid);

        List<LeoDeployingVO.PodDetails> pods = KubernetesPodDriver.list(kubernetesConfig.getKubernetes(), namespace, deploymentName)
                .stream()
                .map(p -> podDetailsHelper.toPodDetails(p, containerName))
                .collect(Collectors.toList());

        oldVersion.setPods(pods);

        deploy.setDeployVersion1(oldVersion);

        LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                .id(leoDeploy.getId())
                .deployConfig(deployConfig.dump())
                .deployStatus("校验Kubernetes阶段: 成功")
                .build();
        save(saveLeoDeploy, "校验Kubernetes成功");
    }

    @Override
    public String getDeployType() {
        return DeployTypeConstants.REDEPLOY.name();
    }

}