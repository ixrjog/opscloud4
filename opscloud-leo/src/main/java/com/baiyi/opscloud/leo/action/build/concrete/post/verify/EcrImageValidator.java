package com.baiyi.opscloud.leo.action.build.concrete.post.verify;

import com.amazonaws.services.ecr.model.ImageDetail;
import com.amazonaws.services.ecr.model.Repository;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.ecr.driver.AmazonEcrImageDriver;
import com.baiyi.opscloud.datasource.aws.ecr.driver.AmazonEcrRepositoryDirver;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.leo.action.build.concrete.post.verify.base.BaseCrImageValidator;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.baiyi.opscloud.leo.action.build.concrete.post.verify.base.BaseCrImageValidator.CRS.ECR;

/**
 * @Author baiyi
 * @Date 2022/11/18 15:12
 * @Version 1.0
 */
@Slf4j
@Component
public class EcrImageValidator extends BaseCrImageValidator<AwsConfig> {

    @Resource
    private AmazonEcrRepositoryDirver amazonEcrRepositoryDirver;

    @Resource
    private AmazonEcrImageDriver amazonEcrImageDriver;

    @Override
    public String getCrType() {
        return ECR;
    }

    @Override
    protected AwsConfig getDsConfigByUuid(String uuid) {
        return getDsConfigByUuid(uuid, AwsConfig.class);
    }

    @Override
    protected void handleVerify(LeoJob leoJob, LeoJobModel.CR cr, LeoBuildModel.BuildConfig buildConfig, AwsConfig dsConfig) {
        LeoJobModel.CRInstance crInstance = cr.getInstance();
        final String crRegionId = cr.getInstance().getRegionId();
        final String crInstanceId = crInstance.getId();
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
                .orElseGet(() -> getCrRegistryId(cr, leoJob, crRegionId, crInstanceId, repoNamespace, repositoryName, dsConfig));
        try {
            // public List<ImageDetail> describeImages(String regionId, AwsConfig.Aws config, String registryId, String repositoryName, int size) {
            List<ImageDetail> imageDetails = amazonEcrImageDriver.describeImages(crRegionId, dsConfig.getAws(), crRegistryId, repositoryName, QUERY_IMAGES_SIZE);
            String imageTag = dict.get(BuildDictConstants.IMAGE_TAG.getKey());
            imageDetails.stream()
                    .filter(imageDetail -> imageDetail.getImageTags().stream().anyMatch(t -> t.equals(imageTag)))
                    .findFirst()
                    .orElseThrow(() -> new LeoBuildException("查询AWS-ECR镜像未找到对应的标签: imageTag={}", imageTag));
        } catch (Exception e) {
            throw new LeoBuildException("查询AWS-ECR镜像错误: err={}", e.getMessage());
        }
    }

    /**
     * 查询注册表ID
     *
     * @param cr
     * @param leoJob
     * @param crRegionId
     * @param crInstanceId
     * @param repoNamespace
     * @param repositoryName
     * @param dsConfig
     * @return
     */
    protected String getCrRegistryId(LeoJobModel.CR cr, LeoJob leoJob, String crRegionId, String crInstanceId, String repoNamespace, String repositoryName, AwsConfig dsConfig) {
        try {
            Repository repository = amazonEcrRepositoryDirver.describeRepository(crRegionId, dsConfig.getAws(), repositoryName);
            if (repository == null) {
                throw new LeoBuildException("AWS-ECR RegistryId查询失败: regionId={}, instanceId={}, repositoryName={}", crRegionId, crInstanceId, repositoryName);
            }
            return repository.getRegistryId();
        } catch (Exception e) {
            throw new LeoBuildException("AWS-ECR RegistryId查询错误: regionId={}, instanceId={}, repositoryName={}, err={}", crRegionId, crInstanceId, repositoryName, e.getMessage());
        }
    }

}