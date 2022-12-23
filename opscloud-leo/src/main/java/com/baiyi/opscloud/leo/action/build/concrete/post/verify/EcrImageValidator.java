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
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.baiyi.opscloud.leo.action.build.concrete.post.verify.base.BaseCrImageValidator.CrTypes.ECR;

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
            List<ImageDetail> imageDetails = amazonEcrImageDriver.describeImages(crRegionId, dsConfig.getAws(), crRegistryId, repositoryName, QUERY_IMAGES_SIZE);
            if (CollectionUtils.isEmpty(imageDetails)) {
                throw new LeoBuildException("查询AWS-ECR镜像列表为空！");
            }
            final String imageTag = dict.get(BuildDictConstants.IMAGE_TAG.getKey());
            imageDetails.stream()
                    .filter(imageDetail -> filterWithImageTag(imageDetail, imageTag))
                    .findFirst()
                    .orElseThrow(() -> new LeoBuildException("查询AWS-ECR镜像未找到对应的标签: imageTag={}", imageTag));
        } catch (Exception e) {
            throw new LeoBuildException("查询AWS-ECR镜像错误: err={}", e.getMessage());
        }
    }

    private boolean filterWithImageTag(ImageDetail imageDetail, String imageTag) {
        if (CollectionUtils.isEmpty(imageDetail.getImageTags())) return false;
        return imageDetail.getImageTags().stream().anyMatch(t -> t.equals(imageTag));
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
            Repository repository = amazonEcrRepositoryDirver.describeRepository(crRegionId, dsConfig.getAws(), repositoryName);
            return Optional.ofNullable(repository)
                    .map(Repository::getRegistryId)
                    .orElseThrow(() -> new LeoBuildException("AWS-ECR RegistryId查询失败: regionId={}, repositoryName={}", crRegionId, repositoryName));
        } catch (Exception e) {
            throw new LeoBuildException("AWS-ECR RegistryId查询错误: regionId={}, repositoryName={}, err={}", crRegionId, repositoryName, e.getMessage());
        }
    }

}