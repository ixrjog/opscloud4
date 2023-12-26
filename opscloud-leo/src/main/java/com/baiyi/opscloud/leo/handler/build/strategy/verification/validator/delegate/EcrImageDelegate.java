package com.baiyi.opscloud.leo.handler.build.strategy.verification.validator.delegate;

import com.amazonaws.services.ecr.model.ImageDetail;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.ecr.driver.AmazonEcrImageDriver;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/1/18 18:10
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EcrImageDelegate {

    private final AmazonEcrImageDriver amazonEcrImageDriver;

    /**
     * 校验镜像是否存在
     *
     * @param crRegionId
     * @param dsConfig
     * @param crRegistryId
     * @param repositoryName
     * @param imageTag
     */
    @Retryable(retryFor = LeoBuildException.class, maxAttempts = 4, backoff = @Backoff(delay = 3000, multiplier = 1))
    public void verify(String crRegionId, AwsConfig dsConfig, String crRegistryId, String repositoryName, String imageTag) throws LeoBuildException {
        try {
            List<ImageDetail> imageDetails = amazonEcrImageDriver.describeImages(crRegionId, dsConfig.getAws(), crRegistryId, repositoryName, imageTag);
            if (CollectionUtils.isEmpty(imageDetails)) {
                log.warn("查询AWS-ECR镜像列表为空 regionId={}, registryId={}, repositoryName={} ", crRegionId, crRegistryId, repositoryName);
                throw new LeoBuildException("查询AWS-ECR镜像列表为空 regionId={}, registryId={}, repositoryName={}", crRegionId, crRegistryId, repositoryName);
            }
            imageDetails.stream()
                    .filter(imageDetail -> filterWithImageTag(imageDetail, imageTag))
                    .findFirst()
                    .orElseThrow(() ->
                            new LeoBuildException("查询AWS-ECR镜像未找到对应的标签 {}", imageTag)
                    );
        } catch (Exception e) {
            log.warn("校验AWS-ECR镜像失败: {}", e.getMessage());
            throw new LeoBuildException("校验AWS-ECR镜像失败: {}", e.getMessage());
        }
    }

    private boolean filterWithImageTag(ImageDetail imageDetail, String imageTag) {
        if (CollectionUtils.isEmpty(imageDetail.getImageTags())) {
            return false;
        }
        return imageDetail.getImageTags().stream().anyMatch(t -> t.equals(imageTag));
    }

}