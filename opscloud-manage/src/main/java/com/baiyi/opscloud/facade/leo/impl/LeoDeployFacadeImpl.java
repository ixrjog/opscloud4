package com.baiyi.opscloud.facade.leo.impl;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.instance.OcInstance;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.datasource.facade.DsInstanceFacade;
import com.baiyi.opscloud.datasource.kubernetes.driver.NewKubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.ApplicationResTypeEnum;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.param.leo.LeoDeployParam;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;
import com.baiyi.opscloud.domain.vo.leo.LeoDeployVO;
import com.baiyi.opscloud.facade.application.ApplicationFacade;
import com.baiyi.opscloud.facade.leo.LeoDeployFacade;
import com.baiyi.opscloud.leo.action.deploy.LeoDeployHandler;
import com.baiyi.opscloud.leo.annotation.LeoDeployInterceptor;
import com.baiyi.opscloud.leo.constants.ExecutionTypeConstants;
import com.baiyi.opscloud.leo.delegate.LeoBuildDeploymentDelegate;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.baiyi.opscloud.leo.interceptor.LeoExecuteJobInterceptorHandler;
import com.baiyi.opscloud.leo.packer.LeoDeployResponsePacker;
import com.baiyi.opscloud.leo.supervisor.DeployingSupervisor;
import com.baiyi.opscloud.packer.leo.LeoBuildVersionPacker;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.LabelSelector;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/12/5 14:31
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LeoDeployFacadeImpl implements LeoDeployFacade {

    public static final String WORKLOAD_SELECTOR_NAME = "workload.user.cattle.io/workloadselector";

    private final LeoJobService jobService;

    private final LeoBuildService buildService;

    private final LeoBuildVersionPacker buildVersionPacker;

    private final LeoDeployService deployService;

    private final LeoDeployHandler deployHandler;

    private final LeoDeployResponsePacker deployResponsePacker;

    private final LeoExecuteJobInterceptorHandler executeJobInterceptorHandler;

    private final RedisUtil redisUtil;

    private final LeoBuildDeploymentDelegate buildDeploymentDelegate;

    private final DsInstanceAssetService assetService;

    private final DsConfigHelper dsConfigHelper;

    private final DsInstanceFacade<Deployment> dsInstanceFacade;

    private final ApplicationFacade applicationFacade;

    @Override
    @LeoDeployInterceptor(jobIdSpEL = "#doDeploy.jobId", deployTypeSpEL = "#doDeploy.deployType")
    public void doDeploy(LeoDeployParam.DoDeploy doDeploy) {
        // 执行部署任务
        LeoJob leoJob = jobService.getById(doDeploy.getJobId());
        final int deployNumber = generateDeployNumberWithJobId(leoJob.getId());

        LeoJobModel.JobConfig jobConfig = LeoJobModel.load(leoJob.getJobConfig());
        // 部署通知
        LeoBaseModel.Notify notify = Optional.ofNullable(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getDeploy)
                .map(LeoJobModel.Deploy::getNotify)
                .orElseThrow(() -> new LeoDeployException("部署通知配置不存在！"));

        LeoBaseModel.Kubernetes kubernetes = LeoBaseModel.Kubernetes.builder()
                .assetId(doDeploy.getAssetId())
                .build();

        LeoDeployModel.Deploy deploy = LeoDeployModel.Deploy.builder()
                .deployType(doDeploy.getDeployType())
                .notify(notify)
                .kubernetes(kubernetes)
                .build();

        LeoDeployModel.DeployConfig deployConfig = LeoDeployModel.DeployConfig.builder()
                .deploy(deploy)
                .build();

        LeoDeploy newLeoDeploy = LeoDeploy.builder()
                .applicationId(leoJob.getApplicationId())
                .jobId(leoJob.getId())
                .jobName(leoJob.getName())
                .buildId(doDeploy.getBuildId() == null ? 0 : doDeploy.getBuildId())
                .deployNumber(deployNumber)
                .deployConfig(deployConfig.dump())
                .executionType(ExecutionTypeConstants.USER)
                .username(SessionUtil.getUsername())
                .isFinish(false)
                .isActive(true)
                .isRollback(false)
                .ocInstance(OcInstance.ocInstance)
                .build();
        deployService.add(newLeoDeploy);
        handleDeploy(newLeoDeploy, deployConfig);
    }

    /**
     * 使用责任链设计模式解耦代码
     *
     * @param leoDeploy
     * @param deployConfig
     */
    private void handleDeploy(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        deployHandler.handleDeploy(leoDeploy, deployConfig);
    }

    /**
     * 生成构建编号
     * 当前最大值 + 1
     *
     * @param jobId
     * @return
     */
    private int generateDeployNumberWithJobId(int jobId) {
        return deployService.getMaxDeployNumberWithJobId(jobId) + 1;
    }

    @Override
    public List<LeoBuildVO.Build> queryLeoDeployVersion(LeoDeployParam.QueryDeployVersion queryBuildVersion) {
        List<LeoBuild> builds = buildService.queryBuildVersion(queryBuildVersion);
        return BeanCopierUtil.copyListProperties(builds, LeoBuildVO.Build.class).stream()
                .peek(e -> buildVersionPacker.wrap(e, queryBuildVersion))
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationResourceVO.BaseResource> queryLeoBuildDeployment(LeoDeployParam.QueryDeployDeployment queryBuildDeployment) {
        return buildDeploymentDelegate.queryLeoBuildDeployment(queryBuildDeployment.getJobId());
    }

    @Override
    public DataTable<LeoDeployVO.Deploy> queryLeoJobDeployPage(LeoJobParam.JobDeployPageQuery pageQuery) {
        List<LeoJob> leoJobs = jobService.queryJobWithApplicationIdAndEnvType(pageQuery.getApplicationId(), pageQuery.getEnvType());
        if (CollectionUtils.isEmpty(leoJobs)) {
            return DataTable.EMPTY;
        }
        List<Integer> jobIds = leoJobs.stream()
                .map(LeoJob::getId)
                .collect(Collectors.toList());
        pageQuery.setJobIds(jobIds);
        DataTable<LeoDeploy> table = deployService.queryDeployPage(pageQuery);
        List<LeoDeployVO.Deploy> data = BeanCopierUtil.copyListProperties(table.getData(), LeoDeployVO.Deploy.class).stream()
                .peek(deployResponsePacker::wrap)
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public void stopDeploy(int deployId) {
        LeoDeploy leoDeploy = deployService.getById(deployId);
        if (leoDeploy == null) {
            throw new LeoDeployException("部署记录不存在: deployId={}", deployId);
        }
        LeoJob leoJob = jobService.getById(leoDeploy.getJobId());
        final String username = SessionUtil.getUsername();
        if (executeJobInterceptorHandler.isAdmin(username)) {
            // 管理员操作，跳过验证
        } else {
            // 权限校验
            executeJobInterceptorHandler.verifyAuthorization(leoJob.getId());
        }
        // 设置信号量
        redisUtil.set(String.format(DeployingSupervisor.STOP_SIGNAL, deployId), username, 100L);
    }

    @Override
    public void cloneDeployDeployment(LeoDeployParam.CloneDeployDeployment cloneDeployDeployment) {
        LeoJob leoJob = jobService.getById(cloneDeployDeployment.getJobId());
        if (leoJob == null) {
            throw new LeoDeployException("任务不存在: jobId={}", cloneDeployDeployment.getJobId());
        }

        DatasourceInstanceAsset asset = assetService.getById(cloneDeployDeployment.getAssetId());
        KubernetesConfig kubernetesConfig = dsConfigHelper.buildKubernetesConfig(asset.getInstanceUuid());
        // 查询原无状态
        final String namespace = asset.getAssetKey2();
        final String deploymentName = asset.getAssetKey();
        if (deploymentName.equals(cloneDeployDeployment.getDeploymentName())) {
            throw new LeoDeployException("原无状态与克隆的无状态名称相同!");
        }
        Deployment deployment = NewKubernetesDeploymentDriver.get(
                kubernetesConfig.getKubernetes(),
                namespace,
                deploymentName);
        if (deployment == null) {
            throw new LeoDeployException("无状态配置 {} 不存在!", cloneDeployDeployment.getDeploymentName());
        }

        preUpdateDeployment(deployment, deploymentName, cloneDeployDeployment.getDeploymentName(), cloneDeployDeployment.getReplicas());
        // 创建无状态
        try {
            NewKubernetesDeploymentDriver.create(kubernetesConfig.getKubernetes(), namespace, deployment);
        } catch (Exception e) {
            throw new LeoDeployException("克隆无状态错误: {}", e.getMessage());
        }

        // 录入资产
        List<DatasourceInstanceAsset> assets = dsInstanceFacade.pullAsset(asset.getInstanceUuid(), DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name(), deployment);
        // 绑定资产到应用
        assets.forEach(a -> {
            ApplicationResourceVO.Resource resource = ApplicationResourceVO.Resource.builder()
                    .applicationId(leoJob.getApplicationId())
                    .businessId(a.getId())
                    .businessType(BusinessTypeEnum.ASSET.getType())
                    .checked(false)
                    .comment(a.getAssetId())
                    .name(a.getAssetId())
                    .resourceType(ApplicationResTypeEnum.KUBERNETES_DEPLOYMENT.name())
                    .virtualResource(false)
                    .build();
            applicationFacade.bindApplicationResource(resource);
        });
    }

    private void preUpdateDeployment(Deployment deployment, String oldName, String newName, Integer replicas) {
        deployment.getMetadata().setName(newName);
        deployment.getSpec().setReplicas(replicas);
        // Pod分组标签
        deployment.getSpec()
                .getTemplate()
                .getMetadata()
                .getLabels()
                .put("group", newName);

        // 更新 Labels
        Map<String, String> labels = Optional.of(deployment)
                .map(Deployment::getMetadata)
                .map(ObjectMeta::getLabels)
                .orElse(Maps.newHashMap());

        if (labels.containsKey(WORKLOAD_SELECTOR_NAME)) {
            final String workloadSelector = labels.get(WORKLOAD_SELECTOR_NAME).replace(oldName, newName);
            try {

                if (Optional.of(deployment)
                        .map(Deployment::getMetadata)
                        .map(ObjectMeta::getLabels)
                        .orElse(Maps.newHashMap())
                        .containsKey(WORKLOAD_SELECTOR_NAME)) {
                    deployment.getMetadata().getLabels().put(WORKLOAD_SELECTOR_NAME, workloadSelector);
                }

                if (Optional.of(deployment)
                        .map(Deployment::getSpec)
                        .map(DeploymentSpec::getSelector)
                        .map(LabelSelector::getMatchLabels)
                        .orElse(Maps.newHashMap()).containsKey(WORKLOAD_SELECTOR_NAME)) {
                    deployment.getSpec().getSelector().getMatchLabels().put(WORKLOAD_SELECTOR_NAME, workloadSelector);
                }

                if (Optional.of(deployment)
                        .map(Deployment::getSpec)
                        .map(DeploymentSpec::getTemplate)
                        .map(PodTemplateSpec::getMetadata)
                        .map(ObjectMeta::getLabels)
                        .orElse(Maps.newHashMap()).containsKey(WORKLOAD_SELECTOR_NAME)) {
                    deployment.getSpec().getTemplate().getMetadata().getLabels().put(WORKLOAD_SELECTOR_NAME, workloadSelector);
                }
            } catch (Exception e) {
                throw new LeoDeployException("清理标签错误: {}", e.getMessage());
            }
        }
    }

    @Override
    public List<LeoDeployVO.Deploy> getLatestLeoDeploy(int size) {
        List<LeoDeploy> deploys = deployService.queryLatestLeoDeploy(size);
        return BeanCopierUtil.copyListProperties(deploys, LeoDeployVO.Deploy.class).stream()
                .peek(deployResponsePacker::wrap)
                .collect(Collectors.toList());
    }

}
