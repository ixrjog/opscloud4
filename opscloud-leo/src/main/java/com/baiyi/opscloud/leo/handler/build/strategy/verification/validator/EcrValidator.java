package com.baiyi.opscloud.leo.handler.build.strategy.verification.validator;

import com.amazonaws.services.ecr.model.Repository;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.ecr.driver.AmazonEcrRepositoryDriver;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.leo.handler.build.strategy.verification.validator.base.BaseCrValidator;
import com.baiyi.opscloud.leo.handler.build.strategy.verification.validator.delegate.EcrImageDelegate;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.google.common.base.Joiner;
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
public class EcrValidator extends BaseCrValidator<AwsConfig> {

    @Resource
    private AmazonEcrRepositoryDriver amazonEcrRepositoryDriver;

    @Resource
    private EcrImageDelegate ecrImageDelegate;

    @Override
    public String getCrType() {
        return CrTypes.ECR;
    }

    @Override
    protected AwsConfig getDsConfigByUuid(String uuid) {
        return getDsConfigByUuid(uuid, AwsConfig.class);
    }

    @Override
    protected void handleVerifyImage(LeoJob leoJob, LeoJobModel.CR cr, LeoBuildModel.BuildConfig buildConfig, AwsConfig dsConfig) {
        final String crRegionId = cr.getInstance().getRegionId();
        final String crRepoName = cr.getRepo().getName();
        Map<String, String> dict = buildConfig.getBuild().getDict();
        final String repoNamespace = Optional.of(cr)
                .map(LeoJobModel.CR::getRepo)
                .map(LeoJobModel.Repo::getNamespace)
                .orElseGet(() -> dict.get(BuildDictConstants.ENV.getKey()));
        // ${evnName}/${repoName}
        final String repositoryName = Joiner.on("/").join(repoNamespace, crRepoName);
        final String crRegistryId = Optional.of(cr)
                .map(LeoJobModel.CR::getRepo)
                .map(LeoJobModel.Repo::getId)
                .orElseGet(() -> getCrRegistryId(cr, leoJob, crRegionId, repoNamespace, repositoryName, dsConfig));
        try {
            final String imageTag = dict.get(BuildDictConstants.IMAGE_TAG.getKey());
            ecrImageDelegate.verify(crRegionId, dsConfig, crRegistryId, repositoryName, imageTag);
        } catch (Exception e) {
            throw new LeoBuildException("查询AWS-ECR镜像错误: {}", e.getMessage());
        }
    }

    @Override
    protected void handleCreateRepository(LeoJob leoJob, LeoJobModel.CR cr, AwsConfig dsConfig) {
        final String crRegionId = cr.getInstance().getRegionId();
        final String crRepoName = cr.getRepo().getName();
        final String repoNamespace = Optional.of(cr)
                .map(LeoJobModel.CR::getRepo)
                .map(LeoJobModel.Repo::getNamespace)
                .orElseGet(() -> envService.getByEnvType(leoJob.getEnvType()).getEnvName());
        // ${evnName}/${repoName}
        final String repositoryName = Joiner.on("/").join(repoNamespace, crRepoName);
        try {
            Repository repository = amazonEcrRepositoryDriver.createRepository(crRegionId, dsConfig.getAws(), repositoryName);
        } catch (Exception e) {
            throw new LeoBuildException("创建ECR仓库错误: {}", e.getMessage());
        }
    }

    /**
     * 查询注册表ID
     *
     * @param cr
     * @param leoJob
     * @param crRegionId
     * @param repoNamespace
     * @param repositoryName
     * @param dsConfig
     * @return
     */
    protected String getCrRegistryId(LeoJobModel.CR cr, LeoJob leoJob, String crRegionId, String repoNamespace, String repositoryName, AwsConfig dsConfig) {
        try {
            Repository repository = amazonEcrRepositoryDriver.describeRepository(crRegionId, dsConfig.getAws(), repositoryName);
            return Optional.ofNullable(repository)
                    .map(Repository::getRegistryId)
                    .orElseThrow(() -> new LeoBuildException("AWS-ECR RegistryId查询失败: regionId={}, repositoryName={}", crRegionId, repositoryName));
        } catch (Exception e) {
            throw new LeoBuildException("AWS-ECR RegistryId查询错误: regionId={}, repositoryName={}, {}", crRegionId, repositoryName, e.getMessage());
        }
    }

}