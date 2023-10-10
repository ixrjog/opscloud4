package com.baiyi.opscloud.leo.supervisor.strategy;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesPodDriver;
import com.baiyi.opscloud.domain.constants.DeployTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.vo.leo.LeoDeployingVO;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.baiyi.opscloud.leo.supervisor.strategy.base.SupervisingStrategy;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.Pod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/12/13 15:30
 * @Version 1.0
 */
@Slf4j
@Component
public class SupervisingWithRollingStrategy extends SupervisingStrategy {

    @Override
    @Retryable(retryFor = LeoDeployException.class, maxAttempts = 2, backoff = @Backoff(delay = 1000, multiplier = 1.5))
    protected LeoDeployingVO.Deploying getDeploying(LeoDeploy leoDeploy,
                                                    LeoDeployModel.DeployConfig deployConfig,
                                                    KubernetesConfig.Kubernetes kubernetes,
                                                    LeoDeployModel.Deploy deploy,
                                                    LeoBaseModel.Deployment deployment) {
        List<Pod> pods = KubernetesPodDriver.list(kubernetes, deployment.getNamespace(), deployment.getName());
        if (CollectionUtils.isEmpty(pods)) {
            return LeoDeployingVO.Deploying.builder().build();
        }
        final String containerName = deployment.getContainer().getName();

        LeoDeployingVO.VersionDetails previousVersion = LeoDeployingVO.VersionDetails.builder()
                .title("Previous Version|以前版本")
                .versionName(deploy.getDeployVersion1().getVersionName())
                .versionDesc(deploy.getDeployVersion1().getVersionDesc())
                .image(deploy.getDeployVersion1().getImage())
                .build();

        LeoDeployingVO.VersionDetails releaseVersion = LeoDeployingVO.VersionDetails.builder()
                .title("Releases Version|发布版本")
                .versionName(deploy.getDeployVersion2().getVersionName())
                .versionDesc(deploy.getDeployVersion2().getVersionDesc())
                .image(deploy.getDeployVersion2().getImage())
                .build();

        for (Pod pod : pods) {
            Optional<Container> optionalContainer = pod.getSpec().getContainers().stream().filter(e -> e.getName().equals(containerName)).findFirst();
            if (optionalContainer.isPresent()) {
                Container container = optionalContainer.get();
                String image = container.getImage();
                LeoDeployingVO.PodDetails podDetails = podDetailsHelper.toPodDetails(pod, containerName);
                // 上个版本
                if (image.equals(previousVersion.getImage())) {
                    previousVersion.putPod(podDetails);
                    continue;
                }
                // 发布版本
                if (image.equals(releaseVersion.getImage())) {
                    releaseVersion.putPod(podDetails);
                }
            } else {
                log.warn("没找到容器: container={}", containerName);
            }
        }

        return LeoDeployingVO.Deploying.builder()
                .deployType(deploy.getDeployType())
                .versionDetails1(previousVersion)
                .versionDetails2(releaseVersion)
                .replicas(deployment.getReplicas())
                .build()
                .init();
    }

    @Override
    protected boolean verifyFinish(LeoDeploy leoDeploy, LeoDeployingVO.Deploying deploying) {
        // 发布结束
        if (deploying.getIsFinish() && CollectionUtils.isEmpty(deploying.getVersionDetails1().getPods())) {
            LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                    .id(leoDeploy.getId())
                    .endTime(new Date())
                    .deployResult("SUCCESS")
                    .deployStatus("执行部署任务阶段: 结束")
                    .isFinish(true)
                    .isActive(true)
                    .build();
            leoDeployService.updateByPrimaryKeySelective(saveLeoDeploy);
            return true;
        }
        return false;
    }

    @Override
    public String getDeployType() {
        return DeployTypeConstants.ROLLING.name();
    }

}