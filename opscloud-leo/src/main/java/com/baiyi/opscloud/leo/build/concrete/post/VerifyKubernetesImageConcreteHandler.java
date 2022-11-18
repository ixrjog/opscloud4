package com.baiyi.opscloud.leo.build.concrete.post;

import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.InstanceHelper;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.datasource.aliyun.acr.driver.AliyunAcrImageDriver;
import com.baiyi.opscloud.datasource.aliyun.acr.driver.AliyunAcrRepositoryDriver;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.leo.build.BaseBuildHandler;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.service.leo.LeoJobService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/11/17 16:11
 * @Version 1.0
 */
@Slf4j
@Component
public class VerifyKubernetesImageConcreteHandler extends BaseBuildHandler {

    @Resource
    private LeoJobService leoJobService;

    @Resource
    private InstanceHelper instanceHelper;

    @Resource
    private DsConfigHelper dsConfigHelper;

    @Resource
    private AliyunAcrRepositoryDriver aliyunAcrRepositoryDriver;

    @Resource
    private AliyunAcrImageDriver aliyunAcrImageDriver;

    private static final String KUBERNETES_IMAGE = "kubernetes-image";

    /**
     * 验证 Kubernetes Image
     *
     * @param leoBuild
     * @param buildConfig
     */
    @Override
    protected void handle(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        LeoJob leoJob = leoJobService.getById(leoBuild.getJobId());
        LeoJobModel.JobConfig jobConfig = LeoJobModel.load(leoJob.getJobConfig());
        final String buildType = Optional.ofNullable(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getBuild)
                .map(LeoJobModel.Build::getType)
                .orElse(KUBERNETES_IMAGE);
        if (!KUBERNETES_IMAGE.equalsIgnoreCase(buildType)) return;

        final LeoJobModel.CR cr = Optional.ofNullable(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getCr)
                .orElseThrow(() -> new LeoBuildException("任务CR配置不存在无法验证镜像是否推送成功！"));

        final String crType = Optional.of(cr)
                .map(LeoJobModel.CR::getType)
                .orElseThrow(() -> new LeoBuildException("任务CR类型配置不存在无法验证镜像是否推送成功！"));
        if ("ACR".equalsIgnoreCase(crType)) {
            verifyKubernetesImageWithACR(cr);
            return;
        }
        if ("ECR".equalsIgnoreCase(crType)) {
            verifyKubernetesImageWithECR();
            return;
        }
        throw new LeoBuildException("任务CR类型配置不正确: crType={}", crType);
    }

    private void verifyKubernetesImageWithACR(LeoJobModel.CR cr) {

        LeoJobModel.CRInstance crInstance = Optional.of(cr)
                .map(LeoJobModel.CR::getInstance)
                .orElseThrow(() -> new LeoBuildException("任务CR Instance配置不存在无法验证镜像是否推送成功！"));

        final String crRegionId = Optional.of(crInstance)
                .map(LeoJobModel.CRInstance::getRegionId)
                .orElseThrow(() -> new LeoBuildException("任务CR Instance RegionId配置不存在无法验证镜像是否推送成功！"));

        final String crInstanceId = Optional.of(crInstance)
                .map(LeoJobModel.CRInstance::getId)
                .orElseThrow(() -> new LeoBuildException("任务CR Instance InstanceId配置不存在无法验证镜像是否推送成功！"));

        final String crRepoId = Optional.of(crInstance)
                .map(LeoJobModel.CRInstance::getId)
                .orElse("1111");

        String uuid = getCloudDsInstanceUuid(cr);
        DatasourceConfig dsConfig = dsConfigHelper.getConfigByInstanceUuid(uuid);
        AliyunConfig aliyunConfig = dsConfigHelper.build(dsConfig, AliyunConfig.class);
        try {
            aliyunAcrImageDriver.listImage(crRegionId, aliyunConfig.getAliyun(), crInstanceId, "repoId", 10);
        } catch (ClientException e) {

        }
    }


    private void verifyKubernetesImageWithECR() {

    }

    private String getCloudDsInstanceUuid(LeoJobModel.CR cr) {
        String uuid = Optional.of(cr)
                .map(LeoJobModel.CR::getCloud)
                .map(LeoJobModel.Cloud::getUuid)
                .orElse("");
        if (StringUtils.isNotBlank(uuid))
            return uuid;
        String name = Optional.of(cr)
                .map(LeoJobModel.CR::getCloud)
                .map(LeoJobModel.Cloud::getName)
                .orElseThrow(() -> new LeoBuildException("任务CR Cloud配置不存在无法验证镜像是否推送成功！"));
        DatasourceInstance dsInstance = instanceHelper.getInstanceByName(name);
        if (dsInstance == null)
            throw new LeoBuildException("任务CR Cloud数据源实例不存在无法验证镜像是否推送成功！");
        return dsInstance.getUuid();
    }

    private String getCrRepoId(LeoJobModel.CRInstance crInstance, AliyunConfig aliyunConfig) {
        String repoId = Optional.of(crInstance)
                .map(LeoJobModel.CRInstance::getId)
                .orElse("");
        if (StringUtils.isNotBlank(repoId)) return repoId;
        //  (String regionId, AliyunConfig.Aliyun aliyun, String instanceId, String repoNamespaceName, String repoName) throws ClientException {

        final String regionId = Optional.of(crInstance)
                .map(LeoJobModel.CRInstance::getRegionId)
                .orElseThrow(() -> new LeoBuildException("任务CRInstace regionId 配置不存在无法验证镜像是否推送成功！"));

        final String repoName = Optional.of(crInstance)
                .map(LeoJobModel.CRInstance::getName)
                .orElseThrow(() -> new LeoBuildException("任务CRInstace repoName 配置不存在无法验证镜像是否推送成功！"));

        try {
            aliyunAcrRepositoryDriver.getRepositoryId(regionId, aliyunConfig.getAliyun(), "111", "daily", repoName);
        } catch (ClientException e) {

        }
        return "";

    }

}
