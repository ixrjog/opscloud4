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
import io.fabric8.kubernetes.api.model.Pod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/12/26 17:29
 * @Version 1.0
 */
@Slf4j
@Component
public class SupervisingWithOfflineStrategy extends SupervisingStrategy {

    @Override
    @Retryable(retryFor = LeoDeployException.class, maxAttempts = 2, backoff = @Backoff(delay = 1000, multiplier = 1.5))
    protected LeoDeployingVO.Deploying getDeploying(LeoDeploy leoDeploy,
                                                    LeoDeployModel.DeployConfig deployConfig,
                                                    KubernetesConfig.Kubernetes kubernetes,
                                                    LeoDeployModel.Deploy deploy,
                                                    LeoBaseModel.Deployment deployment) {
        List<Pod> pods = KubernetesPodDriver.list(kubernetes, deployment.getNamespace(), deployment.getName());
        final String containerName = deployment.getContainer().getName();
        final String image = Optional.of(deployConfig)
                .map(LeoDeployModel.DeployConfig::getDeploy)
                .map(LeoDeployModel.Deploy::getKubernetes)
                .map(LeoBaseModel.Kubernetes::getDeployment)
                .map(LeoBaseModel.Deployment::getContainer)
                .map(LeoBaseModel.Container::getImage)
                .orElse("N/A");

        LeoDeployingVO.VersionDetails offlineVersion = LeoDeployingVO.VersionDetails.builder()
                .title("Offline|下线中")
                .image(image)
                .build();

        for (Pod pod : pods) {
            LeoDeployingVO.PodDetails podDetails = podDetailsHelper.toPodDetails(pod, containerName);
            offlineVersion.putPod(podDetails);
        }

        return LeoDeployingVO.Deploying.builder()
                .deployType(deploy.getDeployType())
                .versionDetails1(offlineVersion)
                .versionDetails2(LeoDeployingVO.VersionDetails.NO_SHOW)
                .replicas(deployment.getReplicas())
                .build()
                .init();
    }

    @Override
    protected boolean verifyFinish(LeoDeploy leoDeploy, LeoDeployingVO.Deploying deploying) {
        // 发布结束
        if (deploying.getIsFinish()) {
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
        return DeployTypeConstants.OFFLINE.name();
    }

}