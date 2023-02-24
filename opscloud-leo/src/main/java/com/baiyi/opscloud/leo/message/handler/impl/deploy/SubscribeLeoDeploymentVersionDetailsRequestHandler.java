package com.baiyi.opscloud.leo.message.handler.impl.deploy;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.leo.response.LeoContinuousDeliveryResponse;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuildImage;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.param.leo.request.SubscribeLeoDeploymentVersionDetailsRequestParam;
import com.baiyi.opscloud.domain.param.leo.request.type.LeoRequestType;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.leo.LeoJobVersionVO;
import com.baiyi.opscloud.leo.delegate.LeoBuildDeploymentDelegate;
import com.baiyi.opscloud.leo.delegate.LeoJobVersionDelegate;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.message.handler.base.BaseLeoContinuousDeliveryRequestHandler;
import com.baiyi.opscloud.leo.packer.LeoDeployResponsePacker;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.leo.LeoBuildImageService;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.internal.guava.Sets;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订阅无状态版本详情
 *
 * @Author baiyi
 * @Date 2023/2/22 18:13
 * @Version 1.0
 */
@Slf4j
@Component
public class SubscribeLeoDeploymentVersionDetailsRequestHandler
        extends BaseLeoContinuousDeliveryRequestHandler<SubscribeLeoDeploymentVersionDetailsRequestParam> {

    @Resource
    private LeoDeployService deployService;

    @Resource
    private LeoJobService jobService;

    @Resource
    private LeoDeployResponsePacker deployResponsePacker;

    @Resource
    private ApplicationService applicationService;

    @Resource
    private ApplicationResourceService applicationResourceService;

    @Resource
    private DsInstanceAssetService assetService;

    @Resource
    private LeoBuildDeploymentDelegate buildDeploymentDelegate;

    @Resource
    private DsConfigHelper dsConfigHelper;

    @Resource
    private LeoBuildImageService buildImageService;

    @Resource
    private LeoJobVersionDelegate jobVersionDelegate;

    @Override
    public String getMessageType() {
        return LeoRequestType.SUBSCRIBE_LEO_DEPLOYMENT_VERSION_DETAILS.name();
    }

    @Override
    public void handleRequest(String sessionId, Session session, String message) {
        SubscribeLeoDeploymentVersionDetailsRequestParam queryParam = toRequestParam(message);
        List<LeoJob> jobs = jobService.queryJobWithApplicationIdAndEnvType(queryParam.getApplicationId(), queryParam.getEnvType());
        Application application = applicationService.getById(queryParam.getApplicationId());
        if (CollectionUtils.isEmpty(jobs)) {
            return;
        }
        List<LeoJobVersionVO.JobVersion> jobVersions = new ArrayList<>(jobs.size());
        for (LeoJob job : jobs) {
            LeoJobVersionVO.JobVersion jobVersion = LeoJobVersionVO.JobVersion.builder()
                    .jobName(job.getName())
                    .envType(job.getEnvType())
                    .deploymentVersions(generateDeploymentVersions(application, job.getId()))
                    .build();
            jobVersionDelegate.wrap(jobVersion);
            jobVersions.add(jobVersion);
        }
        LeoContinuousDeliveryResponse response = LeoContinuousDeliveryResponse.builder()
                .body(jobVersions)
                .messageType(getMessageType())
                .build();
        sendToSession(session, response);
    }

    private List<LeoJobVersionVO.DeploymentVersion> generateDeploymentVersions(Application application, int jobId) {
        List<ApplicationResourceVO.BaseResource> deploymentResources = buildDeploymentDelegate.queryLeoBuildDeployment(jobId);
        List<LeoJobVersionVO.DeploymentVersion> deploymentVersions = deploymentResources.stream().map(deploymentResource -> {
            DatasourceInstanceAsset asset = assetService.getById(deploymentResource.getBusinessId());
            if (asset == null) {
                return LeoJobVersionVO.DeploymentVersion.EMPTY;
            }
            try {
                KubernetesConfig kubernetesConfig = dsConfigHelper.buildKubernetesConfig(asset.getInstanceUuid());
                Deployment deployment = KubernetesDeploymentDriver.getDeployment(
                        kubernetesConfig.getKubernetes(),
                        // namespace
                        asset.getAssetKey2(),
                        // deploymentName
                        asset.getAssetKey());
                if (deployment == null) {
                    return LeoJobVersionVO.DeploymentVersion.EMPTY;
                }
                Optional<Container> optionalContainer = deployment.getSpec().getTemplate().getSpec().getContainers()
                        .stream()
                        .filter(c -> c.getName().startsWith(application.getName()))
                        .findFirst();

                if (optionalContainer.isPresent()) {
                    final String image = optionalContainer.get().getImage();
                    LeoBuildImage buildImage = buildImageService.findBuildImage(jobId, image);
                    final int replicas = deployment.getSpec().getReplicas();
                    return LeoJobVersionVO.DeploymentVersion.builder()
                            .name(deploymentResource.getName())
                            .buildId(buildImage != null ? buildImage.getBuildId() : -1)
                            .image(image)
                            .replicas(deployment.getSpec().getReplicas())
                            .versionName(buildImage != null ? buildImage.getVersionName() : LeoDeployModel.DeployVersion.UNKNOWN.getVersionName())
                            .versionDesc(buildImage != null ? buildImage.getVersionDesc() : LeoDeployModel.DeployVersion.UNKNOWN.getVersionDesc())
                            .build();
                } else {
                    return LeoJobVersionVO.DeploymentVersion.builder()
                            .name(deploymentResource.getName())
                            .replicas(deployment.getSpec().getReplicas())
                            .build();
                }
            } catch (Exception e) {
                return LeoJobVersionVO.DeploymentVersion.EMPTY;
            }
        }).collect(Collectors.toList());
        renderVersion(deploymentVersions);
        return deploymentVersions;
    }

    /**
     * 渲染版本
     *
     * @param deploymentVersions
     */
    private void renderVersion(List<LeoJobVersionVO.DeploymentVersion> deploymentVersions) {
        if (CollectionUtils.isEmpty(deploymentVersions)) {
            return;
        }

        Set<Integer> idSet = Sets.newHashSet();

        for (LeoJobVersionVO.DeploymentVersion deploymentVersion : deploymentVersions) {
            if (deploymentVersion.getBuildId() == -1) {
                if (deploymentVersion.getReplicas() == 0) {
                    deploymentVersion.setVersionColor(LeoJobVersionVO.VersionColors.OFFLINE);
                } else {
                    // 无法比较版本号
                    deploymentVersion.setVersionColor(LeoJobVersionVO.VersionColors.OTHER);
                }
            } else {
                idSet.add(deploymentVersion.getBuildId());
            }
        }
        if (idSet.isEmpty()) {
            return;
        }
        List<Integer> ids = idSet.stream().sorted(Collections.reverseOrder()).collect(Collectors.toList());
        for (int i = 0; i < ids.size(); i++) {
            int buildId = ids.get(i);
            for (LeoJobVersionVO.DeploymentVersion deploymentVersion : deploymentVersions) {
                if (deploymentVersion.getBuildId() == buildId) {
                    if (deploymentVersion.getReplicas() == 0) {
                        deploymentVersion.setVersionColor(LeoJobVersionVO.VersionColors.OFFLINE);
                        continue;
                    }
                    if (i == 0) {
                        deploymentVersion.setVersionColor(LeoJobVersionVO.VersionColors.BLUE);
                    } else if (i == 1) {
                        deploymentVersion.setVersionColor(LeoJobVersionVO.VersionColors.GREEN);
                    } else {
                        deploymentVersion.setVersionColor(LeoJobVersionVO.VersionColors.OTHER);
                    }
                }
            }
        }
    }

    private SubscribeLeoDeploymentVersionDetailsRequestParam toRequestParam(String message) {
        return toLeoRequestParam(message, SubscribeLeoDeploymentVersionDetailsRequestParam.class);
    }

}
