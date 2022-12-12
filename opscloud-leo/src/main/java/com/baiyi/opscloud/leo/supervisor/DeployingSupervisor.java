package com.baiyi.opscloud.leo.supervisor;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesPodDriver;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.vo.leo.LeoDeployingVO;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.baiyi.opscloud.leo.helper.DeployingLogHelper;
import com.baiyi.opscloud.leo.packer.PodDetailsPacker;
import com.baiyi.opscloud.leo.supervisor.base.ISupervisor;
import com.baiyi.opscloud.leo.util.SnapshotStash;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerStatus;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.domain.vo.leo.LeoDeployingVO.MAX_RESTART;

/**
 * 部署监督
 * @Author baiyi
 * @Date 2022/12/6 14:52
 * @Version 1.0
 */
@Slf4j
public class DeployingSupervisor implements ISupervisor {

    private static final int SLEEP_SECONDS = 10;

    private final LeoDeployService leoDeployService;

    private final LeoDeployModel.DeployConfig deployConfig;

    private final LeoDeploy leoDeploy;

    private final KubernetesConfig.Kubernetes kubernetes;

    private final DeployingLogHelper logHelper;

    private final PodDetailsPacker podDetailsPacker;

    private final SnapshotStash snapshotStash;

    public DeployingSupervisor(LeoDeployService leoDeployService,
                               LeoDeploy leoDeploy,
                               DeployingLogHelper logHelper,
                               LeoDeployModel.DeployConfig deployConfig,
                               KubernetesConfig.Kubernetes kubernetes,
                               PodDetailsPacker podDetailsPacker,
                               SnapshotStash snapshotStash) {
        this.leoDeployService = leoDeployService;
        this.leoDeploy = leoDeploy;
        this.logHelper = logHelper;
        this.deployConfig = deployConfig;
        this.kubernetes = kubernetes;
        this.podDetailsPacker = podDetailsPacker;
        this.snapshotStash = snapshotStash;
    }

    @Override
    public void run() {
        LeoDeployModel.Deploy deploy = Optional.ofNullable(this.deployConfig)
                .map(LeoDeployModel.DeployConfig::getDeploy)
                .orElseThrow(() -> new LeoDeployException("部署配置不存在！"));
        LeoBaseModel.Deployment deployment = Optional.of(deploy)
                .map(LeoDeployModel.Deploy::getKubernetes)
                .map(LeoBaseModel.Kubernetes::getDeployment)
                .orElseThrow(() -> new LeoDeployException("Kubernetes配置不存在！"));

        final String containerName = deployment.getContainer().getName();
        while (true) {
            try {
                List<Pod> pods = KubernetesPodDriver.listPod(kubernetes, deployment.getNamespace(), deployment.getName());
                if (CollectionUtils.isEmpty(pods)) {
                    continue;
                }
                LeoDeployingVO.VerionDetails previousVersion = LeoDeployingVO.VerionDetails.builder()
                        .versionName(deploy.getPreviousVersion().getVersionName())
                        .versionDesc(deploy.getPreviousVersion().getVersionDesc())
                        .image(deploy.getPreviousVersion().getImage())
                        .build();
                LeoDeployingVO.VerionDetails releaseVersion = LeoDeployingVO.VerionDetails.builder()
                        .versionName(deploy.getReleaseVersion().getVersionName())
                        .versionDesc(deploy.getReleaseVersion().getVersionDesc())
                        .image(deploy.getReleaseVersion().getImage())
                        .build();

                for (Pod pod : pods) {
                    Optional<Container> optionalContainer = pod.getSpec().getContainers().stream().filter(e -> e.getName().equals(containerName)).findFirst();
                    if (optionalContainer.isPresent()) {
                        Container container = optionalContainer.get();
                        String image = container.getImage();
                        LeoDeployingVO.PodDetails podDetails = toPodDetails(pod, containerName);
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

                LeoDeployingVO.Deploying deploying = LeoDeployingVO.Deploying.builder()
                        .deployType(deploy.getDeployType())
                        .versionDetails1(previousVersion)
                        .versionDetails2(releaseVersion)
                        .replicas(deployment.getReplicas())
                        .build();
                deploying.init();
                // 缓存
                snapshotStash.save(leoDeploy.getId(), deploying);
                // 发布结束
                if (deploying.getIsFinish() && CollectionUtils.isEmpty(deploying.getVersionDetails1().getPods())) {
                    LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                            .id(this.leoDeploy.getId())
                            .endTime(new Date())
                            .deployResult("SUCCESS")
                            .deployStatus("执行部署任务阶段: 结束")
                            .isFinish(true)
                            .isActive(true)
                            .build();
                    leoDeployService.updateByPrimaryKeySelective(saveLeoDeploy);
                    break;
                }
                if (deploying.isMaxRestartError()) {
                    LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                            .id(this.leoDeploy.getId())
                            .endTime(new Date())
                            .deployResult("ERROR")
                            .deployStatus(String.format("执行部署任务阶段: 容器重启次数超过最大值 maxRestart=%s", MAX_RESTART))
                            .isFinish(true)
                            .isActive(false)
                            .build();
                    leoDeployService.updateByPrimaryKeySelective(saveLeoDeploy);
                    break;
                }
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
            // 延迟执行
            try {
                TimeUnit.SECONDS.sleep(SLEEP_SECONDS);
            } catch (InterruptedException ie) {
                log.warn(ie.getMessage());
            }
        }
    }

    private LeoDeployingVO.PodDetails toPodDetails(Pod pod, String containerName) {
        // 容器状态
        Optional<ContainerStatus> optionalContainerStatus = pod.getStatus().getContainerStatuses().stream().filter(e -> e.getName().equals(containerName)).findFirst();

        Date startTime = com.baiyi.opscloud.core.util.TimeUtil.toDate(pod.getStatus().getStartTime(), TimeZoneEnum.UTC);
        LeoDeployingVO.PodDetails podDetails = LeoDeployingVO.PodDetails.builder()
                .podIP(pod.getStatus().getPodIP())
                .hostIP(pod.getStatus().getHostIP())
                .phase(pod.getStatus().getPhase())
                .reason(pod.getStatus().getReason())
                .name(pod.getMetadata().getName())
                .namespace(pod.getMetadata().getNamespace())
                .terminating(optionalContainerStatus.isPresent() && optionalContainerStatus.get().getState().getTerminated() != null)
                .startTime(startTime)
                .conditions(pod.getStatus().getConditions().stream().collect(Collectors.toMap(PodCondition::getType, PodCondition::getStatus, (k1, k2) -> k1)))
                .restartCount(optionalContainerStatus.isPresent() ? optionalContainerStatus.get().getRestartCount() : 0)
                .build();
        podDetails.init();
        podDetailsPacker.wrap(podDetails);
        return podDetails;
    }

}
