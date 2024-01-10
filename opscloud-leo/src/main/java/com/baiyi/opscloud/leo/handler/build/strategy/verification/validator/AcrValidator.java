package com.baiyi.opscloud.leo.handler.build.strategy.verification.validator;

import com.aliyuncs.cr.model.v20181201.GetRepoTagResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.acr.driver.AliyunAcrImageDriver;
import com.baiyi.opscloud.datasource.aliyun.acr.driver.AliyunAcrRepositoryDriver;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.leo.handler.build.strategy.verification.validator.base.BaseCrValidator;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/11/18 15:12
 * @Version 1.0
 */
@Slf4j
@Component
public class AcrValidator extends BaseCrValidator<AliyunConfig> {

    @Resource
    private AliyunAcrRepositoryDriver aliyunAcrRepositoryDriver;

    @Resource
    private AliyunAcrImageDriver aliyunAcrImageDriver;

    @Override
    public String getCrType() {
        return CrTypes.ACR;
    }

    @Override
    protected AliyunConfig getDsConfigByUuid(String uuid) {
        return getDsConfigByUuid(uuid, AliyunConfig.class);
    }

    @Override
    protected void preInspection(LeoJob leoJob, LeoJobModel.CR cr, LeoBuildModel.BuildConfig buildConfig) {
        super.preInspection(leoJob, cr, buildConfig);
        Optional.of(cr)
                .map(LeoJobModel.CR::getInstance)
                .map(LeoJobModel.CRInstance::getId)
                .orElseThrow(() -> new LeoBuildException("Configuration does not exist: job->cr->instance->id"));
    }

    @Override
    protected void handleVerifyImage(LeoJob leoJob, LeoJobModel.CR cr, LeoBuildModel.BuildConfig buildConfig, AliyunConfig dsConfig) {
        LeoJobModel.CRInstance crInstance = cr.getInstance();
        final String crRegionId = cr.getInstance().getRegionId();
        final String crInstanceId = crInstance.getId();
        final String crRepoName = cr.getRepo().getName();
        Map<String, String> dict = buildConfig.getBuild().getDict();
        final String imageTag = dict.get(BuildDictConstants.IMAGE_TAG.getKey());
        final String envName = dict.get(BuildDictConstants.ENV.getKey());
        final String crRepoId = Optional.of(cr)
                .map(LeoJobModel.CR::getRepo)
                .map(LeoJobModel.Repo::getId)
                .orElseGet(() -> getCrRepoId(cr, leoJob, crRegionId, crInstanceId, crRepoName, dsConfig, envName));
        try {
            Optional.of(aliyunAcrImageDriver.getImage(crRegionId, dsConfig.getAliyun(), crInstanceId, crRepoId, imageTag))
                    .map(GetRepoTagResponse::getImageId)
                    .orElseThrow(() -> new LeoBuildException("阿里云ACR中未找到构建镜像: instanceId={}, repoId={}, imageTag={}", crInstanceId, crRepoId, imageTag));
        } catch (ClientException e) {
            throw new LeoBuildException("查询阿里云-ACR镜像错误: {}", e.getMessage());
        }
    }

    @Override
    protected void handleCreateRepository(LeoJob leoJob, LeoJobModel.CR cr, AliyunConfig dsConfig) {
        // TODO ACR 可开启主动创建
    }

    private String getCrRepoId(LeoJobModel.CR cr, LeoJob leoJob, String crRegionId, String crInstanceId, String crRepoName, AliyunConfig dsConfig, String envName) {
        final String repoNamespace = Optional.of(cr)
                .map(LeoJobModel.CR::getRepo)
                .map(LeoJobModel.Repo::getNamespace)
                .orElse(envName);
        try {
            return aliyunAcrRepositoryDriver.getRepositoryId(crRegionId, dsConfig.getAliyun(), crInstanceId, repoNamespace, crRepoName);
        } catch (ClientException e) {
            throw new LeoBuildException("任务ACR-RepoId查询失败: regionId={}, instanceId={}, repoNamespace={}, repoName={}", crRegionId, crInstanceId, repoNamespace, crRepoName);
        }
    }

}