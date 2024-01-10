package com.baiyi.opscloud.leo.message.handler.impl.deploy;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.leo.response.LeoContinuousDeliveryResponse;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.leo.request.SubscribeLeoDeploymentVersionDetailsRequestParam;
import com.baiyi.opscloud.domain.param.leo.request.type.LeoRequestType;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.leo.LeoJobVersionVO;
import com.baiyi.opscloud.leo.delegate.LeoBuildDeploymentDelegate;
import com.baiyi.opscloud.leo.delegate.LeoJobVersionDelegate;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.message.handler.base.BaseLeoContinuousDeliveryRequestHandler;
import com.baiyi.opscloud.leo.message.util.VersionRenderer;
import com.baiyi.opscloud.leo.packer.DeploymentVersionPacker;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetPropertyService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.leo.LeoBuildImageService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import jakarta.annotation.Resource;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.leo.constants.BuildTypeConstants.KUBERNETES_IMAGE;

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
    private LeoJobService jobService;

    @Resource
    private ApplicationService applicationService;

    @Resource
    private DsInstanceAssetService assetService;

    @Resource
    private LeoBuildDeploymentDelegate buildDeploymentDelegate;

    @Resource
    private DsConfigManager dsConfigManager;

    @Resource
    private LeoBuildImageService buildImageService;

    @Resource
    private LeoJobVersionDelegate jobVersionDelegate;

    @Resource
    private DsInstanceAssetPropertyService propertyService;

    @Resource
    private DeploymentVersionPacker deploymentVersionPacker;

    @Override
    public String getMessageType() {
        return LeoRequestType.SUBSCRIBE_LEO_DEPLOYMENT_VERSION_DETAILS.name();
    }

    @Override
    public void handleRequest(String sessionId, Session session, String message) throws IOException {
        SubscribeLeoDeploymentVersionDetailsRequestParam queryParam = toRequestParam(message);
        List<LeoJobVersionVO.JobVersion> jobVersions = queryLeoJobVersion(queryParam);
        if (CollectionUtils.isEmpty(jobVersions)) {
            return;
        }
        LeoContinuousDeliveryResponse<List<LeoJobVersionVO.JobVersion>> response = LeoContinuousDeliveryResponse.<List<LeoJobVersionVO.JobVersion>>builder()
                .body(jobVersions)
                .messageType(getMessageType())
                .build();
        sendToSession(session, response);
    }

    public List<LeoJobVersionVO.JobVersion> queryLeoJobVersion(SubscribeLeoDeploymentVersionDetailsRequestParam queryParam) {
        List<LeoJob> jobs = jobService.queryJobWithSubscribe(queryParam.getApplicationId(), queryParam.getEnvType(), KUBERNETES_IMAGE);
        Application application = applicationService.getById(queryParam.getApplicationId());
        if (CollectionUtils.isEmpty(jobs)) {
            return Collections.emptyList();
        }
        List<LeoJobVersionVO.JobVersion> jobVersions = new ArrayList<>(jobs.size());
        for (LeoJob job : jobs) {
            LeoJobVersionVO.JobVersion jobVersion = LeoJobVersionVO.JobVersion.builder()
                    .jobName(job.getName())
                    .applicationId(queryParam.getApplicationId())
                    .envType(job.getEnvType())
                    .deploymentVersions(generateDeploymentVersions(application, job.getId()))
                    .build();
            jobVersionDelegate.wrap(jobVersion);
            jobVersions.add(jobVersion);
        }
        return jobVersions;
    }

    private List<LeoJobVersionVO.DeploymentVersion> generateDeploymentVersions(Application application, int jobId) {
        // 查询任务绑定的Deployment
        List<ApplicationResourceVO.BaseResource> deploymentResources = buildDeploymentDelegate.queryLeoBuildDeployment(jobId);
        List<LeoJobVersionVO.DeploymentVersion> deploymentVersions = deploymentResources.stream().map(deploymentResource -> {
                    DatasourceInstanceAsset asset = assetService.getById(deploymentResource.getBusinessId());
                    if (asset == null) {
                        return LeoJobVersionVO.DeploymentVersion.EMPTY;
                    }
                    try {
                        KubernetesConfig kubernetesConfig = dsConfigManager.buildKubernetesConfig(asset.getInstanceUuid());
                        Deployment deployment = KubernetesDeploymentDriver.get(
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
                                .filter(c -> c.getName().startsWith(application.getName()) || c.getName().contains(application.getName()))
                                .findFirst();

                        if (optionalContainer.isPresent()) {
                            final String image = optionalContainer.get().getImage();
                            LeoBuildImage buildImage = buildImageService.findBuildImage(jobId, image);
                            final int replicas = deployment.getSpec().getReplicas();

                            Map<String, String> properties = propertyService.queryByAssetId(asset.getId())
                                    .stream()
                                    .collect(Collectors.toMap(DatasourceInstanceAssetProperty::getName, DatasourceInstanceAssetProperty::getValue, (k1, k2) -> k1));

                            return LeoJobVersionVO.DeploymentVersion.builder()
                                    .jobId(jobId)
                                    .assetId(asset.getId())
                                    .name(deploymentResource.getName())
                                    .deploymentName(asset.getName())
                                    .buildId(buildImage != null ? buildImage.getBuildId() : -1)
                                    .image(image)
                                    .replicas(replicas)
                                    .versionName(buildImage != null ? buildImage.getVersionName() : LeoDeployModel.DeployVersion.UNKNOWN.getVersionName())
                                    .versionDesc(buildImage != null ? buildImage.getVersionDesc() : LeoDeployModel.DeployVersion.UNKNOWN.getVersionDesc())
                                    .properties(properties)
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
                }).toList()
                .stream()
                .peek(e -> deploymentVersionPacker.wrap(e))
                .toList();
        VersionRenderer.render(deploymentVersions);
        return deploymentVersions;
    }

    private SubscribeLeoDeploymentVersionDetailsRequestParam toRequestParam(String message) {
        return toLeoRequestParam(message, SubscribeLeoDeploymentVersionDetailsRequestParam.class);
    }

}